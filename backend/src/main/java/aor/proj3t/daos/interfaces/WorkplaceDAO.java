package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.Workplace;

/**
 * Data Access Object interface for Workplace entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide operations for managing
 * workplace entities within the organization. Workplace entities represent
 * physical locations, offices, departments, or organizational units where
 * employees are based or work from.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Standard CRUD operations for workplace management</li>
 * <li>Location-based employee organization</li>
 * <li>Workplace assignment and tracking</li>
 * <li>Organizational structure support</li>
 * </ul>
 * <p>
 * Note: This interface currently inherits all functionality from
 * {@link BasicDAO}.
 * Additional methods for location-specific queries, workplace statistics, or
 * geographical operations can be added as needed for specific business
 * requirements.
 * </p>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see Workplace
 * @see BasicDAO
 */
public interface WorkplaceDAO extends BasicDAO<Workplace, Long> {

    // Currently inherits all methods from BasicDAO
    // Additional workplace-specific methods can be added here as needed
    // Examples:
    // List<Workplace> findByLocation(String location);
    // List<Workplace> findByCity(String city);
    // Long countEmployeesByWorkplace(Long workplaceId);
    // List<User> findUsersByWorkplace(Long workplaceId);

}
