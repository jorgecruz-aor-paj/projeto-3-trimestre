package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.Optional;

import aor.proj3t.daos.interfaces.PendingRegistrationDAO;
import aor.proj3t.entities.PendingRegistration;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

/**
 * Stateless EJB implementation of the {@link PendingRegistrationDAO} interface.
 * <p>
 * This class provides concrete implementation for managing pending user
 * registration requests within the application. It extends {@link AbstractDAO}
 * to leverage common CRUD operations and implements specific business logic
 * for registration workflow management and cleanup operations.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Email-based registration lookup for verification workflows</li>
 * <li>Automatic cleanup of expired registration requests</li>
 * <li>Safe handling of registration retrieval with Optional return types</li>
 * <li>Time-based expiration management using Instant comparisons</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see PendingRegistrationDAO
 * @see AbstractDAO
 * @see PendingRegistration
 */
@Stateless
public class PendingRegistrationDAOimpl extends AbstractDAO<PendingRegistration, Long>
        implements PendingRegistrationDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code PendingRegistration.class} to enable type-safe
     *           operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<PendingRegistration> getEntityClass() {
        return PendingRegistration.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses JPQL query to find pending registrations by email
     * address.
     * Uses safe result handling by checking the result list and returning the first
     * match wrapped in an Optional to handle cases where no registration exists.
     * </p>
     *
     * @implNote Uses {@link TypedQuery#getResultList()} with stream processing for
     *           safe result handling. This approach avoids NoResultException while
     *           providing clean Optional-based API for callers.
     */
    @Override
    public Optional<PendingRegistration> findByEmail(String email) {
        TypedQuery<PendingRegistration> query = em.createQuery(
                "SELECT p FROM PendingRegistration p WHERE p.email = :email", PendingRegistration.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses a bulk DELETE query with time-based filtering to remove
     * all registration requests that have exceeded their validity period. This
     * method should be called periodically to maintain database hygiene.
     * </p>
     *
     * @implNote Uses {@link Instant#now()} for current time comparison and
     *           {@link jakarta.persistence.Query#executeUpdate()} for efficient
     *           bulk deletion. The method operates directly on the database
     *           level without loading entities into memory.
     */
    @Override
    public void deleteExpired() {
        String jpql = "DELETE FROM PendingRegistration p WHERE p.expirationDate < :now";
        em.createQuery(jpql)
                .setParameter("now", Instant.now())
                .executeUpdate();
    }

}
