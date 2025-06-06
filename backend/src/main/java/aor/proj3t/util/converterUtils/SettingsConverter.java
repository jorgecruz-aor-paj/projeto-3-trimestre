package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.SettingsDTO;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Converter for Settings entities and DTOs.
 * 
 * This class handles the bidirectional conversion between {@link Settings}
 * entities and
 * {@link SettingsDTO} data transfer objects. Settings define system-wide
 * configuration
 * parameters like expiration windows for sessions, password resets, and account
 * confirmations.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class SettingsConverter extends AbstractConverter<Settings, SettingsDTO, Long> {

    @Override
    protected Class<Settings> getEntityClass() {
        return Settings.class;
    }

    @Override
    protected Settings createEntityInstance() {
        return new Settings();
    }

    @Override
    protected SettingsDTO createDTOInstance() {
        return new SettingsDTO();
    }

    @Override
    protected Long getIdFromDTO(SettingsDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(Settings settings, SettingsDTO dto) {
        dto.setId(settings.getId());
        dto.setConfirmationWindow(settings.getConfirmationExpirationTime());
        dto.setSessionWindow(settings.getSessionExpirationTime());
        dto.setResetPasswordWindow(settings.getResetPasswordExpirationTime());
        dto.setCreatedAt(settings.getCreatedAt());

        if (settings.getCreatedBy() != null) {
            dto.setCreatedById(settings.getCreatedBy().getId());
        }
    }

    @Override
    protected void mapDTOToEntity(SettingsDTO dto, Settings settings) {
        settings.setConfirmationExpirationTime(dto.getConfirmationWindow());
        settings.setSessionExpirationTime(dto.getSessionWindow());
        settings.setResetPasswordExpirationTime(dto.getResetPasswordWindow());
        settings.setCreatedAt(dto.getCreatedAt());

        // Handle relationships using findSafely
        User createdBy = findSafely(User.class, dto.getCreatedById());
        settings.setCreatedBy(createdBy);
    }
}
