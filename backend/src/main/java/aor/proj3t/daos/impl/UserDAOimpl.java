package aor.proj3t.daos.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.entities.User;
import aor.proj3t.filters.UserFilter;
import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Implementation of the UserDAO interface that provides data access
 * functionality for User entities.
 * This class extends AbstractDAO to leverage common database operations and
 * implements
 * the UserDAO interface to provide user-specific data access methods.
 * 
 * <p>
 * This implementation uses a combination of helper methods from AbstractDAO and
 * direct Criteria API
 * for more complex queries. Simple lookups use the helper methods like
 * findSingleByProperty(),
 * while complex filtering operations use direct Criteria API for more
 * flexibility.
 * </p>
 * 
 * <p>
 * The class provides methods for:
 * <ul>
 * <li>Looking up users by email</li>
 * <li>Finding users by manager</li>
 * <li>Performing various user counts (total, with/without manager)</li>
 * <li>Complex filtering with multiple conditions</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * UserDAOimpl userDAO = new UserDAOimpl();
 * Optional<User> user = userDAO.findByEmail("user@example.com");
 * List<User> subordinates = userDAO.findByManager(managerId);
 * </pre>
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @see AbstractDAO
 * @see UserDAO
 * @see User
 */
@Stateless
public class UserDAOimpl extends AbstractDAO<User, Long> implements UserDAO {
    /**
     * Find a user by their email address.
     * 
     * @param email The email address to search for
     * @return Optional containing the user if found, empty otherwise
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return findSingleByProperty("email", email);
    }

    /**
     * Find all users managed by a specific manager.
     * 
     * @param managerId The ID of the manager
     * @return A list of users who report to the specified manager
     */
    @Override
    public List<User> findByManager(Long managerId) {
        return findByRelatedProperty("manager", "id", managerId);
    }

    /**
     * Check if all users in the system have a manager assigned.
     * 
     * @return true if all users have a manager assigned, false if any users have
     *         null manager
     */
    @Override
    public boolean allUsersHaveManager() {
        Long count = executeCountQuery("manager", null, true);
        return count == 0;
    }

    /**
     * Count the total number of users in the system.
     * 
     * @return The total number of users
     */
    @Override
    public Long countAllUsers() {
        return executeCountQuery(null, null, false);
    }

    /**
     * Count the number of users that don't have a manager assigned.
     * 
     * @return The count of users with null manager
     */
    @Override
    public Long countUsersWithoutManager() {
        return executeCountQuery("manager", null, true);
    }

    /**
     * Count the number of users that have a manager assigned.
     * 
     * @return The count of users with a non-null manager
     */
    @Override
    public Long countUsersWithManager() {
        return executeCountQueryNotNull("manager");
    }

    /**
     * Get users based on multiple filter criteria.
     * 
     * <p>
     * This method allows for complex filtering of users based on various properties
     * including ID, manager, workplace, and text search across name and email
     * fields.
     * Results can be sorted according to the sort field and direction specified in
     * the filter.
     * </p>
     * 
     * @param filters The filter criteria to apply
     * @return A list of users matching all specified criteria
     */
    @Override
    public List<User> getUsersByFilters(UserFilter filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filters.getId() != null) {
            predicates.add(cb.equal(root.get("id"), filters.getId()));
        }

        if (filters.getManager() != null) {
            predicates.add(cb.equal(root.get("manager"), filters.getManager()));
        }

        if (filters.isNoManager()) {
            predicates.add(cb.isNull(root.get("manager")));
        }

        if (filters.getWorkplace() != null) {
            predicates.add(cb.equal(root.get("workplace"), filters.getWorkplace()));
        }

        if (filters.getSearchText() != null && !filters.getSearchText().isEmpty()) {
            Predicate namePredicate = cb.or(
                    cb.like(root.get("firstName"), "%" + filters.getSearchText() + "%"),
                    cb.like(root.get("lastName"), "%" + filters.getSearchText() + "%"),
                    cb.like(root.get("email"), "%" + filters.getSearchText() + "%"));
            predicates.add(namePredicate);
        }

        query.where(predicates.toArray(Predicate[]::new));

        if (filters.getSortField() != null) {
            query.orderBy(filters.isAscending()
                    ? cb.asc(root.get(filters.getSortField().getFieldName()))
                    : cb.desc(root.get(filters.getSortField().getFieldName())));
        }

        return em.createQuery(query).getResultList();
    }

}
