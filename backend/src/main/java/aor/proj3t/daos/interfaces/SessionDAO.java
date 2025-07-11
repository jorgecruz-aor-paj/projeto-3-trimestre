package aor.proj3t.daos.interfaces;

import java.util.Optional;

import aor.proj3t.entities.Session;

/**
 * Data Access Object interface for Session entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide operations for managing
 * user authentication sessions. Sessions represent active user login states
 * and are used for maintaining user authentication across HTTP requests.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Token-based session lookup for authentication</li>
 * <li>Session creation and management</li>
 * <li>Session expiration and cleanup</li>
 * <li>User authentication state tracking</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see Session
 * @see BasicDAO
 */
public interface SessionDAO extends BasicDAO<Session, Long> {

    /**
     * Finds a session by its authentication token.
     * <p>
     * This method is crucial for user authentication processes. It locates
     * an active session using the authentication token provided by the client.
     * The token is typically sent in HTTP headers or cookies for stateless
     * authentication.
     * </p>
     *
     * @param token The authentication token to search for. Must not be null or
     *              empty.
     * @return An {@link Optional} containing the session if found and valid,
     *         or {@link Optional#empty()} if no session with the token exists
     * @throws IllegalArgumentException               if token is null or empty
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Optional<Session> findByToken(String token);
}
