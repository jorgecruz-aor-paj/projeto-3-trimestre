package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.ResetPasswordDAO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.ResetPassword;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ResetPasswordDAOimpl extends AbstractDAO<ResetPassword, Long> implements ResetPasswordDAO {


    @Override
    public Optional<ResetPassword> findByToken(String token) {
        TypedQuery<ResetPassword> query = em.createQuery(
                "SELECT rp FROM ResetPassword rp WHERE rp.resetPasswordToken = :token", ResetPassword.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }


    @Override
    public Optional<ResetPassword> findByOwnerId(Long ownerId) {
        TypedQuery<ResetPassword> query = em.createQuery(
                "SELECT rp FROM ResetPassword rp WHERE rp.user.id = :ownerId", ResetPassword.class);
        query.setParameter("ownerId", ownerId);
        return query.getResultList().stream().findFirst();
    }


    @Override
    public Boolean isValidToken(String token) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(rp) FROM ResetPassword rp WHERE rp.resetPasswordToken = :token AND p.expirationDate > CURRENT_TIMESTAMP",
                Long.class);
        query.setParameter("token", token);
        return query.getSingleResult() > 0;
    }
}
