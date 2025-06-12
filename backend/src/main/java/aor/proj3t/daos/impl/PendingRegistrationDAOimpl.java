package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.Optional;

import aor.proj3t.daos.interfaces.PendingRegistrationDAO;
import aor.proj3t.entities.PendingRegistration;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

@Stateless
public class PendingRegistrationDAOimpl extends AbstractDAO<PendingRegistration, Long>
        implements PendingRegistrationDAO {

    @Override
    protected Class<PendingRegistration> getEntityClass() {
        return PendingRegistration.class;
    }

    @Override
    public Optional<PendingRegistration> findByToken(String token) {
        TypedQuery<PendingRegistration> query = em.createQuery(
                "SELECT p FROM PendingRegistration p WHERE p.confirmationToken = :token", PendingRegistration.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<PendingRegistration> findByEmail(String email) {
        TypedQuery<PendingRegistration> query = em.createQuery(
                "SELECT p FROM PendingRegistration p WHERE p.email = :email", PendingRegistration.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Boolean isValidToken(String token) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM PendingRegistration p WHERE p.confirmationToken = :token AND p.expirationDate > CURRENT_TIMESTAMP",
                Long.class);
        query.setParameter("token", token);
        return query.getSingleResult() > 0;
    }

    @Override
    public void deleteExpired() {
        String jpql = "DELETE FROM PendingRegistration p WHERE p.expirationDate < :now";
        em.createQuery(jpql)
                .setParameter("now", Instant.now())
                .executeUpdate();
    }


}
