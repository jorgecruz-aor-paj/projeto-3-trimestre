package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.Session;

import java.util.Optional;

public interface SessionDAO extends BasicDAO<Session, Long> {

    Optional<PendingRegistration> findByToken(String token);
    Optional<PendingRegistration> findByEmail(String email);
    Boolean isValidToken(String token);
}
