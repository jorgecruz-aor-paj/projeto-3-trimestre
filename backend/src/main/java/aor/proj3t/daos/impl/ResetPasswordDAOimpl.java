package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.ResetPasswordDAO;
import aor.proj3t.entities.ResetPassword;

import java.util.Optional;

public class ResetPasswordDAOimpl extends AbstractDAO<ResetPassword, Long> implements ResetPasswordDAO {
    @Override
    public Optional<ResetPassword> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public Boolean isValidToken(String token) {
        return null;
    }
}
