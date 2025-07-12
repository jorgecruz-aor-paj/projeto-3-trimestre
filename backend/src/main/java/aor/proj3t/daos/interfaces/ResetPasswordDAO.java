package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.ResetPassword;

import java.util.Optional;

/**
 * Data Access Object interface for ResetPassword entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide operations for managing
 * password reset tokens and requests. ResetPassword entities represent
 * temporary
 * tokens generated when users request to reset their passwords, typically sent
 * via email for secure password recovery processes.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Standard CRUD operations for password reset tokens</li>
 * <li>Token-based password recovery support</li>
 * <li>Secure password reset workflow management</li>
 * <li>Integration with email notification systems</li>
 * </ul>
 * <p>
 * Note: This interface currently inherits all functionality from
 * {@link BasicDAO}.
 * Additional methods for token validation, expiration checking, or email-based
 * lookups may be added as needed for specific business requirements.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see ResetPassword
 * @see BasicDAO
 */
public interface ResetPasswordDAO extends BasicDAO<ResetPassword, Long> {

    Optional<ResetPassword> findByToken(String token);


}
