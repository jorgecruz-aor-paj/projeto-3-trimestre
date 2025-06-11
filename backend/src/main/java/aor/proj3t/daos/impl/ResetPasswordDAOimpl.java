package aor.proj3t.daos.impl;

import java.util.Optional;

import aor.proj3t.daos.interfaces.ResetPasswordDAO;
import aor.proj3t.entities.ResetPassword;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

@Stateless
public class ResetPasswordDAOimpl extends AbstractDAO<ResetPassword, Long> implements ResetPasswordDAO {

    @Override
    protected Class<ResetPassword> getEntityClass() {
        return ResetPassword.class;
    }

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
