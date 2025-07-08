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



}
