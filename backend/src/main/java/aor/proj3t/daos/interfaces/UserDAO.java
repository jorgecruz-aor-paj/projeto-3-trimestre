package aor.proj3t.daos.interfaces;


import aor.proj3t.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 */
public interface UserDAO extends BasicDAO<User, Long> {

    /**
     * Find a user by their email.
     *
     * @param email The email to search for
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all Userss managed by a specific manager.
     *
     * @param managerId The ID of the manager
     * @return A list of users managed by the specified manager
     */
    List<User> findByManager(Long managerId);

    /**
     * Find users by their verification token.
     *
     * @param token The verification token
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findByVerificationToken(String token);

    /**
     * Find users by their password reset token.
     *
     * @param token The password reset token
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findByPasswordResetToken(String token);

    /**
     * Check if all users have a manager assigned.
     *
     * @return true if all users have a manager, false otherwise
     */
    boolean allUsersHaveManager();

    /**
     * Get the count of all users.
     *
     * @return The number of users in the system
     */
    long countUsers();
}
