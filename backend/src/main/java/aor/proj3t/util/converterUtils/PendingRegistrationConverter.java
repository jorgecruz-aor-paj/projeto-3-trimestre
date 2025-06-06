package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.PendingRegistrationDTO;
import aor.proj3t.entities.PendingRegistration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class PendingRegistrationConverter implements Converter<PendingRegistration, PendingRegistrationDTO> {

    @Inject
    private EntityManager em;

    @Override
    public PendingRegistrationDTO toDTO(PendingRegistration pendingRegistration) {
        if (pendingRegistration == null) {
            return null;
        }

        return new PendingRegistrationDTO(
                pendingRegistration.getId(),
                pendingRegistration.getEmail(),
                pendingRegistration.getCreatedAt(),
                pendingRegistration.getConfirmationToken(),
                pendingRegistration.getExpirationDate());
    }

    @Override
    public PendingRegistration toEntity(PendingRegistrationDTO dto) {
        if (dto == null) {
            return null;
        }

        PendingRegistration pendingRegistration;
        if (dto.getId() != 0) {
            pendingRegistration = em.find(PendingRegistration.class, dto.getId());
            if (pendingRegistration == null) {
                pendingRegistration = new PendingRegistration();
            }
        } else {
            pendingRegistration = new PendingRegistration();
        }

        pendingRegistration.setEmail(dto.getEmail());
        pendingRegistration.setCreatedAt(dto.getCreatedAt());
        pendingRegistration.setConfirmationToken(dto.getConfirmationToken());
        pendingRegistration.setExpirationDate(dto.getExpirationDate());

        return pendingRegistration;
    }
}
