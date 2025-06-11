package aor.proj3t.daos.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import aor.proj3t.daos.interfaces.BasicDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public abstract class AbstractDAO<T, ID> implements BasicDAO<T, ID> {

    @PersistenceContext(unitName = "jorgeosorio-proj3t")
    protected EntityManager em;

    public final Class<T> entityClass;

    protected AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T create(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return em.merge(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

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
     * Execute a named query with parameters.
     *
     * @param queryName   The name of the query
     * @param entityClass The type of entity used
     * @param params      The parameters for the query in format: paramName1,
     *                    paramValue1, paramName2, paramValue2, etc.
     * @return A list of results
     */
    protected List<T> executeNamedQuery(String queryName, Class<T> entityClass, Object... params) {
        TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
        for (int i = 0; i < params.length; i += 2) {
            query.setParameter(params[i].toString(), params[i + 1]);
        }
        return query.getResultList();
    }

    /**
     * Execute a named query with parameters that returns a single result.
     *
     * @param queryName The name of the query
     * @param params    The parameters for the query in format: paramName1,
     *                  paramValue1, paramName2, paramValue2, etc.
     * @return An Optional containing the result if found, or empty if not found
     */
    protected Optional<T> executeSingleResultNamedQuery(String queryName, Object... params) {
        try {
            TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
            for (int i = 0; i < params.length; i += 2) {
                query.setParameter(params[i].toString(), params[i + 1]);
            }
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Execute a count query using Criteria API.
     * Counts entities based on a property.
     *
     * @param propertyName The property to filter on (null for no filtering)
     * @param value        The value to filter by (ignored if propertyName is null)
     * @param isNull       If true, checks if propertyName is null (ignores value)
     * @return Count of matching entities
     */
    protected Long executeCountQuery(String propertyName, Object value, boolean isNull) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
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
     * Find entities by property using Criteria API.
     * This method retrieves a list of entities that match a specific property
     * value.
     *
     * @param propertyName Property name to filter on
     * @param value        Value to match
     * @return List of matching entities
     */
    protected List<T> findByProperty(String propertyName, Object value) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        cq.where(cb.equal(root.get(propertyName), value));
        return em.createQuery(cq).getResultList();
    }

    /**
     * Find a single entity by property using Criteria API.
     * This method retrieves a single entity that matches a specific property
     * value.
     * If no entity is found, it returns an empty Optional.
     * 
     * @param propertyName Property name to filter on
     * @param value        Value to match
     * @return Optional containing the entity if found
     */
    protected Optional<T> findSingleByProperty(String propertyName, Object value) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
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
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
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
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        cq.where(cb.isNotNull(root.get(propertyName)));
        return em.createQuery(cq).getSingleResult();
    }
}