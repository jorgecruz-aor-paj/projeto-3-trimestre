package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.SessionDAO;
import aor.proj3t.entities.Session;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class SessionDAOimpl extends AbstractDAO<Session, Long> implements SessionDAO {

    @Override
    protected Class<Session> getEntityClass() {
        return Session.class;
    }

    @Override
    public Optional<Session> findByToken(String token) {
        TypedQuery<Session> query = em.createQuery(
                "SELECT s FROM Session s WHERE s.sessionToken = :token", Session.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Boolean isValidToken(String token) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(s) FROM Session s WHERE s.sessionToken = :token AND s.expirationDate > CURRENT_TIMESTAMP",
                Long.class);
        query.setParameter("token", token);
        return query.getSingleResult() > 0;
    }

    @Override
    public List<Session> findSessionsByUserId(Long userId) {
        TypedQuery<Session> query = em.createQuery(
                "SELECT s FROM Session s WHERE s.user.id = :userId", Session.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public Integer deleteSessionsByUserId(Long userId) {
        Query query = em.createQuery(
                "DELETE FROM Session s WHERE s.user.id = :userId"
        );
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }


}
