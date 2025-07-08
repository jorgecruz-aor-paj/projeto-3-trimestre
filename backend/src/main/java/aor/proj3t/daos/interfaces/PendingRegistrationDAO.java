package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.PendingRegistration;

import java.util.Optional;

public interface PendingRegistrationDAO extends BasicDAO<PendingRegistration,Long> {


    Optional<PendingRegistration> findByEmail(String email);
    void deleteExpired();

}
