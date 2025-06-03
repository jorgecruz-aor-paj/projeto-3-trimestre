package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.PendingRegistrationDAO;
import aor.proj3t.entities.PendingRegistration;

import java.util.Optional;

public class PendingRegistrationDAOimpl extends AbstractDAO<PendingRegistration, Long> implements PendingRegistrationDAO {
    @Override
    public Optional<PendingRegistration> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<PendingRegistration> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Boolean isValidToken(String token) {
        return null;
    }
}
