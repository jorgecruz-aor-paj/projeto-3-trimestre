package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.ResetPasswordDTO;
import aor.proj3t.entities.ResetPassword;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResetPasswordConverter extends AbstractConverter<ResetPassword, ResetPasswordDTO, Long> {

    @Override
    protected Class<ResetPassword> getEntityClass() {
        return ResetPassword.class;
    }

    @Override
    protected ResetPassword createEntityInstance() {
        return new ResetPassword();
    }

    @Override
    protected ResetPasswordDTO createDTOInstance() {
        return new ResetPasswordDTO();
    }

    @Override
    protected Long getIdFromDTO(ResetPasswordDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(ResetPassword resetPassword, ResetPasswordDTO dto) {
        dto.setId(resetPassword.getId());
        dto.setCreatedAt(resetPassword.getCreatedAt());
        dto.setExpirationDate(resetPassword.getExpirationDate());
        dto.setResetPasswordToken(resetPassword.getResetPasswordToken());

        if (resetPassword.getUser() != null) {
            dto.setUserId(resetPassword.getUser().getId());
        }
    }

    @Override
    protected void mapDTOToEntity(ResetPasswordDTO dto, ResetPassword resetPassword) {
        resetPassword.setCreatedAt(dto.getCreatedAt());
        resetPassword.setExpirationDate(dto.getExpirationDate());
        resetPassword.setResetPasswordToken(dto.getResetPasswordToken());

        // Handle relationships using findSafely
        User user = findSafely(User.class, dto.getUserId());
        resetPassword.setUser(user);
    }
}
