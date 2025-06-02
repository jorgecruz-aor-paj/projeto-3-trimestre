package aor.proj3t.daos.impl;


import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserRepository interface.
 */
@Stateless
public class UserDAOimpl extends AbstractDAO <User, Long> implements UserDAO {

    @Override
    public Optional<User> findByEmail(String email) {
        return executeSingleResultNamedQuery("User.findByEmail", "email", email);
    }

    @Override
    public List<User> findByManager(Long managerId) {
        return executeNamedQuery("User.findByManager", "managerId", managerId);
    }

    @Override
    public Optional<User> findByVerificationToken(String token) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.verificationToken = :token", User.class);
            query.setParameter("token", token);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByPasswordResetToken(String token) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.passwordResetToken = :token", User.class);
            query.setParameter("token", token);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean allUsersHaveManager() {
        Query query = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.manager IS NULL AND u.role = 'USER'");
        long count = (long) query.getSingleResult();
        return count == 0;
    }

    @Override
    public long countUsers() {
        Query query = em.createQuery("SELECT COUNT(u) FROM User u");
        return (long) query.getSingleResult();
    }
}

