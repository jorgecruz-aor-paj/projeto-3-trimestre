package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.SessionDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class SessionConverter implements Converter<Session, SessionDTO> {

    @Inject
    private EntityManager em;

    @Override
    public SessionDTO toDTO(Session session) {
        if (session == null) {
            return null;
        }

        return new SessionDTO(
                session.getId(),
                session.getCreatedAt(),
                session.getExpirationDate(),
                session.getSessionToken(),
                session.getActive(),
                session.getIpAddress(),
                session.getUserAgent(),
                session.getUser() != null ? session.getUser().getId() : null);
    }

    @Override
    public Session toEntity(SessionDTO dto) {
        if (dto == null) {
            return null;
        }

        Session session;
        if (dto.getId() != null && dto.getId() != 0) {
            session = em.find(Session.class, dto.getId());
            if (session == null) {
                session = new Session();
            }
        } else {
            session = new Session();
        }

        session.setCreatedAt(dto.getCreatedAt());
        session.setExpirationDate(dto.getExpirationDate());
        session.setSessionToken(dto.getSessionToken());
        session.setActive(dto.getActive());
        session.setIpAddress(dto.getIpAddress());
        session.setUserAgent(dto.getUserAgent());

        // Handle relationships
        if (dto.getUserId() != null) {
            User user = em.find(User.class, dto.getUserId());
            session.setUser(user);
        }

        return session;
    }
}
