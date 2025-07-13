package aor.proj3t.daos.interfaces;

import java.util.Optional;

import aor.proj3t.entities.TwoFactorAuth;
import aor.proj3t.entities.User;

/**
 * Data Access Object interface for TwoFactorAuth entity operations.
 * Provides methods for CRUD operations and specialized queries for 2FA
 * management.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public interface TwoFactorAuthDAO extends BasicDAO<TwoFactorAuth, Long> {

    /**
     * Finds the 2FA configuration for a specific user.
     * 
     * @param user the user to find 2FA configuration for
     * @return Optional containing the TwoFactorAuth if found
     */
    Optional<TwoFactorAuth> findByUser(User user);

    /**
     * Finds the 2FA configuration by user ID.
     * 
     * @param userId the ID of the user
     * @return Optional containing the TwoFactorAuth if found
     */
    Optional<TwoFactorAuth> findByUserId(Long userId);

    /**
     * Checks if a user has 2FA enabled and verified.
     * 
     * @param userId the ID of the user
     * @return true if 2FA is active (enabled and verified)
     */
    boolean isUserTwoFactorEnabled(Long userId);

    /**
     * Deletes the 2FA configuration for a specific user.
     * 
     * @param userId the ID of the user
     * @return true if the configuration was deleted, false if not found
     */
    boolean deleteByUserId(Long userId);
}
