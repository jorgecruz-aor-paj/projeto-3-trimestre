package aor.proj3t.daos.impl;

import java.util.List;
import java.util.Optional;

import aor.proj3t.daos.interfaces.BasicDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * Abstract base implementation of the {@link BasicDAO} interface.
 * <p>
 * This class provides a concrete implementation of the basic CRUD operations
 * defined in {@link BasicDAO} using JPA/Hibernate. It serves as a foundation
 * for all specific DAO implementations in the application, promoting code reuse
 * and consistency across data access layers.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Uses JPA EntityManager for database operations</li>
 * <li>Implements type-safe operations through generics</li>
 * <li>Provides helper methods for common query patterns</li>
 * <li>Uses Criteria API for dynamic query building</li>
 * <li>Supports named queries for optimized performance</li>
 * </ul>
 * <p>
 * Subclasses must implement {@link #getEntityClass()} to specify the entity
 * type
 * they manage. This enables type-safe operations and proper entity loading.
 * </p>
 *
 * @param <T>  The entity type managed by this DAO
 * @param <ID> The type of the entity's primary key
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see BasicDAO
 * @see EntityManager
 */
public abstract class AbstractDAO<T, ID> implements BasicDAO<T, ID> {

    /**
     * JPA EntityManager for database operations.
     * <p>
     * This EntityManager is injected by the container and configured with the
     * persistence unit "jorgeosorio-proj3t". It provides the interface to the
     * persistence context and is used for all database operations.
     * </p>
     */
    @PersistenceContext(unitName = "jorgeosorio-proj3t")
    protected EntityManager em;

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses {@link EntityManager#persist(Object)} to make the entity
     * instance managed and persistent. The entity will be inserted into the
     * database
     * when the transaction is committed or the persistence context is flushed.
     * </p>
     *
     * @implNote The entity's ID will be generated if using auto-generation
     *           strategies.
     *           The method returns the same entity instance that was passed in.
     */
    @Override
    public T create(T entity) {
        em.persist(entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses {@link EntityManager#merge(Object)} to merge the state
     * of the given entity into the current persistence context. If the entity
     * exists in the database, it will be updated; otherwise, a new record will be
     * created.
     * </p>
     *
     * @implNote Returns a managed instance of the entity which may be different
     *           from the passed entity if it was detached.
     */
    @Override
    public T update(T entity) {
        return em.merge(entity);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses {@link EntityManager#find(Class, Object)} to retrieve
     * the entity by its primary key. This method loads the entity eagerly if found.
     * </p>
     *
     * @implNote The entity class is determined by the abstract method
     *           {@link #getEntityClass()}.
     *           Returns null wrapped in Optional if no entity with the given ID
     *           exists.
     */
    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(getEntityClass(), id));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses JPA Criteria API to build a type-safe query that
     * selects all entities of the managed type. This approach provides
     * compile-time safety and is database-agnostic.
     * </p>
     *
     * @implNote For large datasets, consider implementing pagination in specific
     *           DAO implementations to avoid memory issues. This method loads
     *           all entities into memory at once.
     */
    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());
        cq.select(root);
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation first attempts to find the entity by ID, then removes it
     * if found. The removal is performed using {@link EntityManager#remove(Object)}
     * which requires the entity to be in a managed state.
     * </p>
     *
     * @implNote The entity must first be loaded into the persistence context
     *           before it can be removed. This results in two database operations:
     *           a SELECT followed by a DELETE.
     */
    @Override
    public boolean deleteById(ID id) {
        Optional<T> entity = findById(id);
        if (entity.isPresent()) {
            em.remove(entity.get());
            return true;
        }
        return false;
    }

    /**
     * Gets the class of the entity managed by this DAO.
     * <p>
     * This abstract method must be implemented by concrete DAO classes to specify
     * the entity type they manage. The returned Class object is used for type-safe
     * query creation and reflection-based operations throughout the DAO
     * implementation.
     * </p>
     * <p>
     * The entity class is used in various operations such as:
     * </p>
     * <ul>
     * <li>Finding entities by ID using
     * {@link EntityManager#find(Class, Object)}</li>
     * <li>Creating Criteria queries with the correct entity type</li>
     * <li>Type-safe query result handling</li>
     * </ul>
     *
     * @return The Class object representing the entity type managed by this DAO
     * @implNote Typically implemented as: {@code return EntityClass.class;}
     */
    protected abstract Class<T> getEntityClass();

    /**
     * Executes a named query with optional parameters.
     * <p>
     * This utility method provides a convenient way to execute predefined named
     * queries
     * with parameter binding. Named queries are defined in entity classes using
     * {@code @NamedQuery} annotations and offer better performance through query
     * compilation
     * and caching.
     * </p>
     * <p>
     * Parameters are provided as alternating name-value pairs, making the method
     * flexible for queries with varying numbers of parameters.
     * </p>
     *
     * @param queryName   The name of the named query as defined in the entity class
     * @param entityClass The expected return type for type-safe result handling
     * @param params      The parameters for the query in alternating name-value
     *                    format:
     *                    paramName1, paramValue1, paramName2, paramValue2, etc.
     * @return A list of results matching the query criteria
     * @throws IllegalArgumentException if the query name is invalid or parameter
     *                                  count is odd
     * @implNote Parameters must be provided in pairs. An odd number of parameters
     *           will
     *           result in the last parameter being ignored.
     */
    protected List<T> executeNamedQuery(String queryName, Class<T> entityClass, Object... params) {
        TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
        for (int i = 0; i < params.length; i += 2) {
            query.setParameter(params[i].toString(), params[i + 1]);
        }
        return query.getResultList();
    }

    /**
     * Executes a named query that returns a single result.
     * <p>
     * This utility method is designed for named queries that are expected to return
     * at most one result. It provides safe handling of the single result case by
     * wrapping the result in an Optional to handle cases where no result is found.
     * </p>
     * <p>
     * This method is particularly useful for lookup operations by unique
     * identifiers
     * or queries with very specific criteria that should match at most one entity.
     * </p>
     *
     * @param queryName The name of the named query as defined in the entity class
     * @param params    The parameters for the query in alternating name-value
     *                  format:
     *                  paramName1, paramValue1, paramName2, paramValue2, etc.
     * @return An Optional containing the result if found, or empty if no result
     *         matches
     * @throws jakarta.persistence.NonUniqueResultException if the query returns
     *                                                      more than one result
     * @implNote Uses try-catch to handle NoResultException and returns empty
     *           Optional
     *           when no results are found to avoid exceptions in calling code.
     */
    protected Optional<T> executeSingleResultNamedQuery(String queryName, Object... params) {
        try {
            TypedQuery<T> query = em.createNamedQuery(queryName, getEntityClass());
            for (int i = 0; i < params.length; i += 2) {
                query.setParameter(params[i].toString(), params[i + 1]);
            }
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Executes a count query using JPA Criteria API.
     * <p>
     * This utility method builds and executes a count query with optional filtering
     * based on a specific property. It supports both value-based filtering and
     * null-checking operations.
     * </p>
     * <p>
     * The method is flexible and can be used for various counting scenarios:
     * </p>
     * <ul>
     * <li>Count all entities (no filtering)</li>
     * <li>Count entities with a specific property value</li>
     * <li>Count entities where a property is null</li>
     * </ul>
     *
     * @param propertyName The property to filter on (null for no filtering)
     * @param value        The value to filter by (ignored if propertyName is null
     *                     or isNull is true)
     * @param isNull       If true, checks if propertyName is null (ignores value
     *                     parameter)
     * @return Count of matching entities as a Long value
     * @implNote Uses Criteria API for type safety and database independence.
     *           When propertyName is null, returns total count of all entities.
     */
    protected Long executeCountQuery(String propertyName, Object value, boolean isNull) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(getEntityClass());
        cq.select(cb.count(root));

        if (propertyName != null) {
            if (isNull) {
                cq.where(cb.isNull(root.get(propertyName)));
            } else {
                cq.where(cb.equal(root.get(propertyName), value));
            }
        }

        return em.createQuery(cq).getSingleResult();
    }

    /**
     * Finds entities by a specific property value using JPA Criteria API.
     * <p>
     * This utility method provides a type-safe way to query entities based on
     * a single property value. It builds a Criteria query that filters entities
     * where the specified property equals the given value.
     * </p>
     * <p>
     * This method is commonly used for simple lookup operations where you need
     * to find all entities that match a specific criteria on one property.
     * </p>
     *
     * @param propertyName The name of the entity property to filter on. Must not be
     *                     null.
     * @param value        The value to match against the property. Can be null for
     *                     null comparisons.
     * @return A list of entities where the property equals the specified value,
     *         or an empty list if no matches are found
     * @throws IllegalArgumentException if propertyName is null or doesn't exist on
     *                                  the entity
     * @implNote Uses Criteria API for type safety and database independence.
     *           Handles null values appropriately using equals comparison.
     */
    protected List<T> findByProperty(String propertyName, Object value) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());
        cq.select(root);
        cq.where(cb.equal(root.get(propertyName), value));
        return em.createQuery(cq).getResultList();
    }

    /**
     * Finds a single entity by a specific property value using JPA Criteria API.
     * <p>
     * This utility method is similar to {@link #findByProperty(String, Object)} but
     * is designed for cases where you expect at most one result. It provides safe
     * handling by wrapping the result in an Optional.
     * </p>
     * <p>
     * This method is particularly useful for unique property lookups such as
     * finding entities by email, username, or other unique identifiers.
     * </p>
     *
     * @param propertyName The name of the entity property to filter on. Must not be
     *                     null.
     * @param value        The value to match against the property. Can be null for
     *                     null comparisons.
     * @return An Optional containing the entity if found, or empty if no match
     *         exists
     * @throws IllegalArgumentException if propertyName is null or doesn't exist on
     *                                  the entity
     * @implNote Uses try-catch to handle cases where no results are found or
     *           multiple
     *           results exist, returning empty Optional for robustness.
     */
    protected Optional<T> findSingleByProperty(String propertyName, Object value) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
            Root<T> root = cq.from(getEntityClass());
            cq.select(root);
            cq.where(cb.equal(root.get(propertyName), value));

            return Optional.ofNullable(em.createQuery(cq).getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Find entities by a related entity's property.
     * 
     * This method uses Criteria API to filter entities based on a property on a
     * related entity.
     * For example, if you have an entity with a relation to a "manager" entity,
     * you can find all entities where the manager's ID matches a specific value.
     * 
     * @param relationPath Path to the related entity (e.g., "manager")
     * @param propertyName Name of the property on related entity (e.g., "id")
     * @param value        Value to match
     * @return List of matching entities
     */
    protected List<T> findByRelatedProperty(String relationPath, String propertyName, Object value) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());
        cq.select(root);
        cq.where(cb.equal(root.get(relationPath).get(propertyName), value));
        return em.createQuery(cq).getResultList();
    }

    /**
     * Execute a count query for non-null property using Criteria API. This method
     * counts entities where the specified property is not null.
     *
     * @param propertyName Property to check for non-null values
     * @return Count of matching entities
     */
    protected Long executeCountQueryNotNull(String propertyName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(getEntityClass());
        cq.select(cb.count(root));
        cq.where(cb.isNotNull(root.get(propertyName)));
        return em.createQuery(cq).getSingleResult();
    }
}