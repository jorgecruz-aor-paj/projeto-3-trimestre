package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.SettingsDTO;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class SettingsConverter implements Converter<Settings, SettingsDTO> {

    @Inject
    private EntityManager em;

    @Override
    public SettingsDTO toDTO(Settings settings) {
        if (settings == null) {
            return null;
        }

        return new SettingsDTO(
                settings.getId(),
                settings.getConfirmationExpirationTime(),
                settings.getSessionExpirationTime(),
                settings.getResetPasswordExpirationTime(),
                settings.getCreatedAt(),
                settings.getCreatedBy() != null ? settings.getCreatedBy().getId() : null);
    }

    @Override
    public Settings toEntity(SettingsDTO dto) {
        if (dto == null) {
            return null;
        }

        Settings settings;
        if (dto.getId() != null && dto.getId() != 0) {
            settings = em.find(Settings.class, dto.getId());
            if (settings == null) {
                settings = new Settings();
            }
        } else {
            settings = new Settings();
        }

        settings.setConfirmationExpirationTime(dto.getConfirmationWindow());
        settings.setSessionExpirationTime(dto.getSessionWindow());
        settings.setResetPasswordExpirationTime(dto.getResetPasswordWindow());
        settings.setCreatedAt(dto.getCreatedAt());

        // Handle relationships
        if (dto.getCreatedById() != null) {
            User createdBy = em.find(User.class, dto.getCreatedById());
            settings.setCreatedBy(createdBy);
        }

        return settings;
    }
}
