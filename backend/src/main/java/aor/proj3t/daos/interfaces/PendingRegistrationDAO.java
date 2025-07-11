package aor.proj3t.daos.interfaces;

import java.util.Optional;

import aor.proj3t.entities.PendingRegistration;

/**
 * Data Access Object interface for PendingRegistration entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide operations for managing
 * user registration requests that are pending approval or email verification.
 * Pending registrations represent incomplete user registration processes that
 * may require administrative approval or user email confirmation.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Finding registrations by email address</li>
 * <li>Automatic cleanup of expired registration requests</li>
 * <li>Registration approval workflow support</li>
 * <li>Email verification process management</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see PendingRegistration
 * @see BasicDAO
 */
public interface PendingRegistrationDAO extends BasicDAO<PendingRegistration, Long> {

    /**
     * Finds a pending registration by email address.
     * <p>
     * This method locates a pending registration record using the email address
     * as the search criteria. Essential for email verification processes and
     * preventing duplicate registration attempts.
     * </p>
     *
     * @param email The email address to search for. Must not be null or empty.
     * @return An {@link Optional} containing the pending registration if found,
     *         or {@link Optional#empty()} if no registration with the email exists
     * @throws IllegalArgumentException               if email is null or empty
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Optional<PendingRegistration> findByEmail(String email);

    /**
     * Deletes all expired pending registrations.
     * <p>
     * This cleanup method removes pending registration records that have exceeded
     * their validity period. Should be called periodically (e.g., via scheduled
     * jobs)
     * to maintain database hygiene and remove stale registration attempts.
     * The expiration criteria is typically based on creation timestamp and
     * configurable timeout values.
     * </p>
     *
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    void deleteExpired();

}
