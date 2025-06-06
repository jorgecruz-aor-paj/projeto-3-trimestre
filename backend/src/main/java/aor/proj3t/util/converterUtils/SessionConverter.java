package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.SessionDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessionConverter extends AbstractConverter<Session, SessionDTO, Long> {

    @Override
    protected Class<Session> getEntityClass() {
        return Session.class;
    }

    @Override
    protected Session createEntityInstance() {
        return new Session();
    }

    @Override
    protected SessionDTO createDTOInstance() {
        return new SessionDTO();
    }

    @Override
    protected Long getIdFromDTO(SessionDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(Session session, SessionDTO dto) {
        dto.setId(session.getId());
        dto.setCreatedAt(session.getCreatedAt());
        dto.setExpirationDate(session.getExpirationDate());
        dto.setSessionToken(session.getSessionToken());
        dto.setActive(session.getActive());
        dto.setIpAddress(session.getIpAddress());
        dto.setUserAgent(session.getUserAgent());

        if (session.getUser() != null) {
            dto.setUserId(session.getUser().getId());
        }
    }

    @Override
    protected void mapDTOToEntity(SessionDTO dto, Session session) {
        session.setCreatedAt(dto.getCreatedAt());
        session.setExpirationDate(dto.getExpirationDate());
        session.setSessionToken(dto.getSessionToken());
        session.setActive(dto.getActive());
        session.setIpAddress(dto.getIpAddress());
        session.setUserAgent(dto.getUserAgent());

        // Handle relationships using findSafely
        User user = findSafely(User.class, dto.getUserId());
        session.setUser(user);
    }
}
