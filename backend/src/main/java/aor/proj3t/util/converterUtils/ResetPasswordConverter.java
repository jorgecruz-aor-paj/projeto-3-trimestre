package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.ResetPasswordDTO;
import aor.proj3t.entities.ResetPassword;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ResetPasswordConverter implements Converter<ResetPassword, ResetPasswordDTO> {

    @Inject
    private EntityManager em;

    @Override
    public ResetPasswordDTO toDTO(ResetPassword resetPassword) {
        if (resetPassword == null) {
            return null;
        }

        return new ResetPasswordDTO(
                resetPassword.getId(),
                resetPassword.getCreatedAt(),
                resetPassword.getExpirationDate(),
                resetPassword.getResetPasswordToken(),
                resetPassword.getUser() != null ? resetPassword.getUser().getId() : null);
    }

    @Override
    public ResetPassword toEntity(ResetPasswordDTO dto) {
        if (dto == null) {
            return null;
        }

        ResetPassword resetPassword;
        if (dto.getId() != null && dto.getId() != 0) {
            resetPassword = em.find(ResetPassword.class, dto.getId());
            if (resetPassword == null) {
                resetPassword = new ResetPassword();
            }
        } else {
            resetPassword = new ResetPassword();
        }

        resetPassword.setCreatedAt(dto.getCreatedAt());
        resetPassword.setExpirationDate(dto.getExpirationDate());
        resetPassword.setResetPasswordToken(dto.getResetPasswordToken());

        // Handle relationships
        if (dto.getUserId() != null) {
            User user = em.find(User.class, dto.getUserId());
            resetPassword.setUser(user);
        }

        return resetPassword;
    }
}
