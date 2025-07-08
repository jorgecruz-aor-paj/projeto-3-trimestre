package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.PendingRegistrationDAO;
import aor.proj3t.entities.PendingRegistration;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.Optional;

@Stateless
public class PendingRegistrationDAOimpl extends AbstractDAO<PendingRegistration, Long>
        implements PendingRegistrationDAO {

    @Override
    protected Class<PendingRegistration> getEntityClass() {
        return PendingRegistration.class;
    }

    @Override
    public Optional<PendingRegistration> findByEmail(String email) {
        TypedQuery<PendingRegistration> query = em.createQuery(
                "SELECT p FROM PendingRegistration p WHERE p.email = :email", PendingRegistration.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteExpired() {
        String jpql = "DELETE FROM PendingRegistration p WHERE p.expirationDate < :now";
        em.createQuery(jpql)
                .setParameter("now", Instant.now())
                .executeUpdate();
    }


}
