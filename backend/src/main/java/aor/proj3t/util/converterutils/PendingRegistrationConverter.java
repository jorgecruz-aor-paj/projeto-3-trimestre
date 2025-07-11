package aor.proj3t.util.converterutils;

import aor.proj3t.dto.PendingRegistrationDTO;
import aor.proj3t.entities.PendingRegistration;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Converter for PendingRegistration entities and DTOs.
 * 
 * This class handles the bidirectional conversion between
 * {@link PendingRegistration} entities and
 * {@link PendingRegistrationDTO} data transfer objects. Pending registrations
 * represent user
 * signup requests that are awaiting email confirmation, including confirmation
 * tokens
 * and expiration dates.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class PendingRegistrationConverter extends AbstractConverter<PendingRegistration, PendingRegistrationDTO, Long> {

    @Override
    public Class<PendingRegistration> getEntityClass() {
        return PendingRegistration.class;
    }

    @Override
    public PendingRegistration createEntityInstance() {
        return new PendingRegistration();
    }

    @Override
    public PendingRegistrationDTO createDTOInstance() {
        return new PendingRegistrationDTO();
    }

    @Override
    public Long getIdFromDTO(PendingRegistrationDTO dto) {
        return dto.getId() != 0 ? dto.getId() : null;
    }

    @Override
    public void mapEntityToDTO(PendingRegistration pendingRegistration, PendingRegistrationDTO dto) {
        dto.setId(pendingRegistration.getId());
        dto.setEmail(pendingRegistration.getEmail());
        dto.setCreatedAt(pendingRegistration.getCreatedAt());
        dto.setConfirmationToken(pendingRegistration.getConfirmationToken());
        dto.setExpirationDate(pendingRegistration.getExpirationDate());
    }

    @Override
    public void mapDTOToEntity(PendingRegistrationDTO dto, PendingRegistration pendingRegistration) {
        pendingRegistration.setEmail(dto.getEmail());
        pendingRegistration.setCreatedAt(dto.getCreatedAt());
        pendingRegistration.setConfirmationToken(dto.getConfirmationToken());
        pendingRegistration.setExpirationDate(dto.getExpirationDate());
    }
}
