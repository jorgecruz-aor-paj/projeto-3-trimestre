package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.ResetPassword;

import java.util.Optional;

public interface ResetPasswordDAO extends BasicDAO<ResetPassword,Long> {

    Optional<ResetPassword> findByToken(String token);
    Optional<ResetPassword> findByOwnerId(Long ownerId);
    Boolean isValidToken(String token);

}
