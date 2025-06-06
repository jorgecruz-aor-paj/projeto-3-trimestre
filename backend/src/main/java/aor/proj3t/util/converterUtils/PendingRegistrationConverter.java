package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.PendingRegistrationDTO;
import aor.proj3t.entities.PendingRegistration;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PendingRegistrationConverter extends AbstractConverter<PendingRegistration, PendingRegistrationDTO, Long> {

    @Override
    protected Class<PendingRegistration> getEntityClass() {
        return PendingRegistration.class;
    }

    @Override
    protected PendingRegistration createEntityInstance() {
        return new PendingRegistration();
    }

    @Override
    protected PendingRegistrationDTO createDTOInstance() {
        return new PendingRegistrationDTO();
    }

    @Override
    protected Long getIdFromDTO(PendingRegistrationDTO dto) {
        return dto.getId() != 0 ? dto.getId() : null;
    }

    @Override
    protected void mapEntityToDTO(PendingRegistration pendingRegistration, PendingRegistrationDTO dto) {
        dto.setId(pendingRegistration.getId());
        dto.setEmail(pendingRegistration.getEmail());
        dto.setCreatedAt(pendingRegistration.getCreatedAt());
        dto.setConfirmationToken(pendingRegistration.getConfirmationToken());
        dto.setExpirationDate(pendingRegistration.getExpirationDate());
    }

    @Override
    protected void mapDTOToEntity(PendingRegistrationDTO dto, PendingRegistration pendingRegistration) {
        pendingRegistration.setEmail(dto.getEmail());
        pendingRegistration.setCreatedAt(dto.getCreatedAt());
        pendingRegistration.setConfirmationToken(dto.getConfirmationToken());
        pendingRegistration.setExpirationDate(dto.getExpirationDate());
    }
}
