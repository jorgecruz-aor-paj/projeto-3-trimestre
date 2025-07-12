package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.ResetPasswordDAO;
import aor.proj3t.entities.ResetPassword;
import aor.proj3t.entities.Session;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

/**
 * Stateless EJB implementation of the {@link ResetPasswordDAO} interface.
 * <p>
 * This class provides concrete implementation for managing password reset
 * tokens within the application. It extends {@link AbstractDAO} to leverage
 * all common CRUD operations for password reset token management.
 * </p>
 * <p>
 * Current implementation features:
 * </p>
 * <ul>
 * <li>Inherits all basic CRUD operations from AbstractDAO</li>
 * <li>Type-safe operations for ResetPassword entities</li>
 * <li>Ready for extension with password reset-specific methods</li>
 * <li>Supports standard token creation, retrieval, and deletion</li>
 * </ul>
 * <p>
 * Future enhancements could include:
 * </p>
 * <ul>
 * <li>Token-based lookup methods (findByToken)</li>
 * <li>Automatic cleanup of expired tokens (deleteExpired)</li>
 * <li>User-based token retrieval (findByUserEmail)</li>
 * <li>Token validation and expiration checking</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see ResetPasswordDAO
 * @see AbstractDAO
 * @see ResetPassword
 */
@Stateless
public class ResetPasswordDAOimpl extends AbstractDAO<ResetPassword, Long> implements ResetPasswordDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code ResetPassword.class} to enable type-safe operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<ResetPassword> getEntityClass() {
        return ResetPassword.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses JPQL query to find ResetPassword by token;
     * Uses safe result handling by checking the result list and returning the first
     * match wrapped in an Optional to avoid NoResultException.
     * </p>
     *
     * @implNote Uses {@link TypedQuery#getResultList()} followed by stream
     *           processing
     *           to safely handle cases where no ResetPassword is found with the given
     *           token.
     *           This approach is more robust than {@code getSingleResult()} which
     *           throws exceptions for empty results.
     */
    @Override
    public Optional<ResetPassword> findByToken(String token){
        TypedQuery<ResetPassword> query = em.createQuery(
                "SELECT rp FROM ResetPassword rp WHERE rp.resetPasswordToken = :token", ResetPassword.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

}
