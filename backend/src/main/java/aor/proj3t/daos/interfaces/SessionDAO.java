package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.Session;

import java.util.List;
import java.util.Optional;

public interface SessionDAO extends BasicDAO<Session, Long> {

    Optional<Session> findByToken(String token);

    Boolean isValidToken(String token);

    List<Session> findSessionsByUserId(Long userId);

    Integer deleteSessionsByUserId(Long userId);
}
