package aor.proj3t.daos.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.UserFilter;
import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Stateless EJB implementation of the {@link UserDAO} interface.
 * <p>
 * This class provides concrete implementation for managing user entities
 * within the application. It extends {@link AbstractDAO} to leverage common
 * CRUD operations and implements specific business logic for user management,
 * organizational hierarchy, and complex filtering operations.
 * </p>
 * <p>
 * This implementation uses a combination of helper methods from AbstractDAO and
 * direct Criteria API for more complex queries. Simple lookups use the helper
 * methods like
 * {@link AbstractDAO#findSingleByProperty(String, Object)}, while complex
 * filtering
 * operations use direct Criteria API for more flexibility.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Email-based user lookup for authentication</li>
 * <li>Management hierarchy support and queries</li>
 * <li>Complex filtering with multiple conditions using Criteria API</li>
 * <li>Named query integration for optimized operations</li>
 * <li>Role-based filtering and organizational structure support</li>
 * <li>Text search across multiple user properties</li>
 * </ul>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * {@code
 * UserDAOimpl userDAO = new UserDAOimpl();
 * Optional<User> user = userDAO.findByEmail("user@example.com");
 * List<User> managers = userDAO.getManagers();
 * }
 * </pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see UserDAO
 * @see AbstractDAO
 * @see User
 * @see UserFilter
 */
@Stateless
public class UserDAOimpl extends AbstractDAO<User, Long> implements UserDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code User.class} to enable type-safe operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation leverages the inherited
     * {@link AbstractDAO#findSingleByProperty(String, Object)}
     * method for efficient and type-safe email-based lookup. This is a common
     * authentication pattern used throughout the application.
     * </p>
     *
     * @implNote Uses the "email" property for filtering, which should be unique
     *           and indexed for optimal performance. The helper method handles
     *           all Criteria API complexity and exception handling.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return findSingleByProperty("email", email);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation leverages the inherited
     * {@link AbstractDAO#executeCountQuery(String, Object, boolean)}
     * method with no filtering to count all users in the system.
     * </p>
     *
     * @implNote Passes null parameters to count all entities without filtering.
     *           This provides an efficient count operation without loading
     *           entities.
     */
    @Override
    public Long countAllUsers() {
        return executeCountQuery(null, null, false);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses the named query "User.countWithoutManagerAndNotAdmin"
     * for optimized counting of users without managers, excluding admin users
     * from the count as they typically don't require management hierarchy.
     * </p>
     *
     * @implNote Uses named query for performance and excludes admin role to
     *           focus on regular employees who should have managers assigned.
     *           The query should be optimized with appropriate database indexes.
     */
    @Override
    public Long countUsersWithoutManager() {
        return em.createNamedQuery("User.countWithoutManagerAndNotAdmin", Long.class)
                .setParameter("adminRole", Role.ADMIN)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses JPA Criteria API for complex, dynamic filtering with
     * multiple optional conditions. This method demonstrates advanced Criteria API
     * usage for building flexible queries based on filter parameters.
     * </p>
     * <p>
     * The method handles various filtering scenarios:
     * </p>
     * <ul>
     * <li>ID-based filtering for specific user lookup</li>
     * <li>Manager hierarchy filtering with priority logic</li>
     * <li>Role-based filtering with admin inclusion control</li>
     * <li>Workplace-based filtering for location queries</li>
     * <li>Text search across multiple fields (firstName, lastName, email)</li>
     * <li>Dynamic sorting based on filter specifications</li>
     * </ul>
     *
     * @implNote The method uses predicate building pattern for flexible query
     *           construction. Manager filtering has priority over noManager flag
     *           to handle specific vs. general filtering requests appropriately.
     */
    @Override
    public List<User> getUsersByFilters(UserFilter filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        // Filter by ID
        if (filters.getId() != null) {
            predicates.add(cb.equal(root.get("id"), filters.getId()));
        }

        // PRIORITY: specific manager overrides 'noManager'
        if (filters.getManager() != null) {
            predicates.add(cb.equal(root.get("manager"), filters.getManager()));
        } else if (filters.isNoManager()) {
            predicates.add(cb.isNull(root.get("manager")));
        }

        // Exclude admins unless explicitly included
        if (!filters.isIncludeAdmin()) {
            predicates.add(cb.notEqual(root.get("role"), Role.ADMIN));
        }

        // Filter by workplace
        if (filters.getWorkplace() != null) {
            predicates.add(cb.equal(root.get("workplace"), filters.getWorkplace()));
        }

        // Search across name and email
        if (filters.getSearchText() != null && !filters.getSearchText().isEmpty()) {
            String text = "%" + filters.getSearchText().toLowerCase() + "%";
            Predicate namePredicate = cb.or(
                    cb.like(cb.lower(root.get("firstName")), text),
                    cb.like(cb.lower(root.get("lastName")), text),
                    cb.like(cb.lower(root.get("email")), text));
            predicates.add(namePredicate);
        }

        // Apply predicates to query
        query.where(predicates.toArray(Predicate[]::new));

        // Sorting
        if (filters.getSortField() != null) {
            Path<?> sortPath = root.get(filters.getSortField().getFieldName());
            query.orderBy(filters.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath));
        }

        return em.createQuery(query).getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses the inherited
     * {@link AbstractDAO#executeNamedQuery(String, Class, Object...)}
     * method with the named query "User.findAllManagers" for optimized manager
     * retrieval.
     * </p>
     *
     * @implNote Uses named query with role parameter for efficient filtering.
     *           The query should be optimized with appropriate indexes on the role
     *           field.
     */
    @Override
    public List<User> getManagers() {
        return executeNamedQuery("User.findAllManagers", User.class, "role", Role.MANAGER);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses the inherited
     * {@link AbstractDAO#executeNamedQuery(String, Class, Object...)}
     * method with the named query "User.findManagersWithoutUsers" to find managers
     * who currently have no subordinates reporting to them.
     * </p>
     *
     * @implNote Uses named query for complex hierarchical filtering. The query
     *           should use subqueries or joins to identify managers without
     *           subordinates,
     *           which is useful for organizational restructuring and workload
     *           balancing.
     */
    @Override
    public List<User> findManagersWithoutUsers() {
        return executeNamedQuery("User.findManagersWithoutUsers", User.class, "role", Role.MANAGER);
    }

}
