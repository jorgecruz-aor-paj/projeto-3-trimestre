package aor.proj3t.daos.interfaces;

import java.util.List;
import java.util.Optional;

import aor.proj3t.entities.Session;

public interface SessionDAO extends BasicDAO<Session, Long> {

    Optional<Session> findByToken(String token);

    Boolean isValidToken(String token);

    List<Session> findSessionByUserId(Long userId);
}
