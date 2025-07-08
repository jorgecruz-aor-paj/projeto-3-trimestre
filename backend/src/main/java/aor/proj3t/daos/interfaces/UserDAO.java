package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.User;
import aor.proj3t.filters.UserFilter;

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
     * Get the count of all users.
     *
     * @return The number of users in the system
     */
    Long countAllUsers();

    Long countUsersWithoutManager();

    List<User> getManagers();

    List<User> getUsersByFilters(UserFilter filters);

    List<User> findManagersWithoutUsers();

}
