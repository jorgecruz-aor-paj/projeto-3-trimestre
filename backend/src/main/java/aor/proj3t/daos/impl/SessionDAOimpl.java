package aor.proj3t.daos.impl;

import java.util.Optional;

import aor.proj3t.daos.interfaces.SessionDAO;
import aor.proj3t.entities.Session;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

/**
 * Stateless EJB implementation of the {@link SessionDAO} interface.
 * <p>
 * This class provides concrete implementation for managing user authentication
 * sessions within the application. It extends {@link AbstractDAO} to leverage
 * common CRUD operations and implements specific business logic for session
 * token lookup and authentication support.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Efficient token-based session lookup for authentication</li>
 * <li>Safe handling of session retrieval with Optional return types</li>
 * <li>Optimized queries for session management operations</li>
 * <li>Support for stateless authentication workflows</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see SessionDAO
 * @see AbstractDAO
 * @see Session
 */
@Stateless
public class SessionDAOimpl extends AbstractDAO<Session, Long> implements SessionDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code Session.class} to enable type-safe operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<Session> getEntityClass() {
        return Session.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses JPQL query to find sessions by their authentication
     * token.
     * Uses safe result handling by checking the result list and returning the first
     * match wrapped in an Optional to avoid NoResultException.
     * </p>
     *
     * @implNote Uses {@link TypedQuery#getResultList()} followed by stream
     *           processing
     *           to safely handle cases where no session is found with the given
     *           token.
     *           This approach is more robust than {@code getSingleResult()} which
     *           throws exceptions for empty results.
     */
    @Override
    public Optional<Session> findByToken(String token) {
        TypedQuery<Session> query = em.createQuery(
                "SELECT s FROM Session s WHERE s.sessionToken = :token", Session.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

}
