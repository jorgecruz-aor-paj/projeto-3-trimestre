package aor.proj3t.daos.impl;

import java.util.Optional;

import aor.proj3t.daos.interfaces.TwoFactorAuthDAO;
import aor.proj3t.entities.TwoFactorAuth;
import aor.proj3t.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * Stateless EJB implementation of the {@link TwoFactorAuthDAO} interface.
 * <p>
 * This class provides concrete implementation for managing Two-Factor
 * Authentication
 * configurations within the application. It extends {@link AbstractDAO} to
 * leverage common
 * CRUD operations and implements specific business logic for 2FA management,
 * user authentication security, and TOTP secret key handling.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>User-based 2FA configuration lookup</li>
 * <li>Status checking for enabled 2FA verification</li>
 * <li>Safe handling of 2FA configuration queries with Optional return
 * types</li>
 * <li>Efficient user-id based operations for authentication flows</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see TwoFactorAuthDAO
 * @see AbstractDAO
 * @see TwoFactorAuth
 */
@Stateless
public class TwoFactorAuthDAOimpl extends AbstractDAO<TwoFactorAuth, Long> implements TwoFactorAuthDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code TwoFactorAuth.class} to enable type-safe operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<TwoFactorAuth> getEntityClass() {
        return TwoFactorAuth.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses a JPQL query to find the 2FA configuration by user
     * entity.
     * This method provides safe lookup with Optional return type to handle cases
     * where users don't have 2FA configured.
     * </p>
     *
     * @implNote Uses entity relationship navigation for efficient querying.
     */
    @Override
    public Optional<TwoFactorAuth> findByUser(User user) {
        if (user == null) {
            return Optional.empty();
        }

        try {
            TypedQuery<TwoFactorAuth> query = em.createQuery(
                    "SELECT tfa FROM TwoFactorAuth tfa WHERE tfa.user = :user",
                    TwoFactorAuth.class);
            query.setParameter("user", user);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses a JPQL query with user ID for efficient lookup.
     * This is commonly used in authentication flows where only the user ID
     * is available from the JWT token.
     * </p>
     *
     * @implNote Uses entity relationship navigation (tfa.user.id) for clean
     *           and efficient querying.
     */
    @Override
    public Optional<TwoFactorAuth> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }

        try {
            TypedQuery<TwoFactorAuth> query = em.createQuery(
                    "SELECT tfa FROM TwoFactorAuth tfa WHERE tfa.user.id = :userId",
                    TwoFactorAuth.class);
            query.setParameter("userId", userId);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses a JPQL query to check if 2FA is both enabled and verified
     * for the specified user. This is crucial for authentication flows to determine
     * if 2FA validation is required.
     * </p>
     *
     * @implNote Uses boolean conditions in JPQL for efficient checking without
     *           loading the full entity.
     */
    @Override
    public boolean isUserTwoFactorEnabled(Long userId) {
        if (userId == null) {
            return false;
        }

        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(tfa) FROM TwoFactorAuth tfa WHERE tfa.user.id = :userId " +
                            "AND tfa.isEnabled = true AND tfa.isVerified = true",
                    Long.class);
            query.setParameter("userId", userId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses a JPQL delete query for efficient removal of 2FA
     * configuration by user ID. This is useful for cleanup operations or
     * when users disable 2FA completely.
     * </p>
     *
     * @implNote Uses bulk delete operation for efficiency and returns boolean
     *           to indicate if any records were affected.
     */
    @Override
    public boolean deleteByUserId(Long userId) {
        if (userId == null) {
            return false;
        }

        try {
            int deletedCount = em.createQuery(
                    "DELETE FROM TwoFactorAuth tfa WHERE tfa.user.id = :userId").setParameter("userId", userId)
                    .executeUpdate();

            return deletedCount > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
