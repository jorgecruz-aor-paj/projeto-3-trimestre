package aor.proj3t.daos.interfaces;

import java.util.List;
import java.util.Optional;

import aor.proj3t.entities.User;
import aor.proj3t.filters.UserFilter;

/**
 * Data Access Object interface for User entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide comprehensive operations
 * for managing user entities within the application. Users represent all
 * individuals in the system including employees, managers, administrators,
 * and other stakeholders.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Email-based user lookup for authentication</li>
 * <li>User counting and statistics</li>
 * <li>Management hierarchy operations</li>
 * <li>Advanced filtering using {@link UserFilter}</li>
 * <li>Organizational structure support</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see User
 * @see UserFilter
 * @see BasicDAO
 */
public interface UserDAO extends BasicDAO<User, Long> {

    /**
     * Finds a user by their email address.
     * <p>
     * This method is crucial for authentication processes and user lookup
     * operations.
     * Email addresses serve as unique identifiers for users in the system and are
     * typically used for login credentials and communication.
     * </p>
     *
     * @param email The email address to search for. Must not be null or empty.
     * @return An {@link Optional} containing the user if found,
     *         or {@link Optional#empty()} if no user with the email exists
     * @throws IllegalArgumentException               if email is null or empty
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Optional<User> findByEmail(String email);

    /**
     * Counts the total number of users in the system.
     * <p>
     * This method provides the total count of all users registered in the system.
     * Useful for system analytics, capacity planning, and dashboard displays.
     * </p>
     *
     * @return The total number of users in the system, or 0 if no users exist
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Long countAllUsers();

    /**
     * Counts users who do not have an assigned manager.
     * <p>
     * This method identifies users in the organizational hierarchy who are not
     * assigned to any manager. Useful for detecting orphaned users in the
     * management structure and ensuring proper organizational hierarchy.
     * </p>
     *
     * @return The count of users without managers, or 0 if all users have managers
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Long countUsersWithoutManager();

    /**
     * Retrieves all users who have manager roles.
     * <p>
     * This method finds all users who are designated as managers in the system.
     * Essential for organizational hierarchy operations, reporting structures,
     * and management-related functionality.
     * </p>
     *
     * @return A {@link List} of users with manager roles,
     *         or an empty list if no managers exist
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<User> getManagers();

    /**
     * Retrieves users based on complex filtering criteria.
     * <p>
     * This method provides advanced querying capabilities using a filter object
     * that can specify multiple criteria such as role, department, status,
     * and other user properties. Essential for user search and administrative
     * operations.
     * </p>
     *
     * @param filters The filter criteria to apply. Must not be null.
     * @return A {@link List} of users matching the filter criteria,
     *         or an empty list if no matches are found
     * @throws IllegalArgumentException               if filters is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     * @see UserFilter
     */
    List<User> getUsersByFilters(UserFilter filters);

    /**
     * Finds managers who do not have any subordinate users.
     * <p>
     * This method identifies managers in the system who currently have no
     * users reporting to them. Useful for organizational restructuring,
     * identifying potential management redundancies, or finding available
     * managers for new team assignments.
     * </p>
     *
     * @return A {@link List} of managers without subordinate users,
     *         or an empty list if all managers have subordinates
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<User> findManagersWithoutUsers();

}
