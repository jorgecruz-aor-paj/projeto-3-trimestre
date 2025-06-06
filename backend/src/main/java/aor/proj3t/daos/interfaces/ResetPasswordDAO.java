package aor.proj3t.daos.interfaces;

import java.util.Optional;

import aor.proj3t.entities.ResetPassword;

public interface ResetPasswordDAO extends BasicDAO<ResetPassword, Long> {

    Optional<ResetPassword> findByToken(String token);

    Optional<ResetPassword> findByOwnerId(Long ownerId);

    Boolean isValidToken(String token);

}
