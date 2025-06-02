package aor.proj3t.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDAO<T, ID> implements BasicDAO<T, ID> {

    @PersistenceContext(unitName = "jorgeosorio-proj3t")
    protected EntityManager em;

    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
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
     * @param queryName The name of the query
     * @param params    The parameters for the query in format: paramName1,
     *                  paramValue1, paramName2, paramValue2, etc.
     * @return A list of results
     */
    protected List<T> executeNamedQuery(String queryName, Object... params) {
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
}