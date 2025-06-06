package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import aor.proj3t.daos.interfaces.EvaluationDAO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.filters.EvaluationFilter;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

/**
 * Implementation of the EvaluationRepository interface.
 */
@Stateless
public class EvaluationDAOimpl extends AbstractDAO<Evaluation, Long> implements EvaluationDAO {

    @Override
    public List<Evaluation> findByEvaluated(Long evaluatedId) {
        TypedQuery<Evaluation> query = em.createQuery(
                "SELECT e FROM Evaluation e WHERE e.evaluated.id = :evaluatedId", Evaluation.class);
        query.setParameter("evaluatedId", evaluatedId);
        return query.getResultList();
    }

    @Override
    public List<Evaluation> findByEvaluator(Long evaluatorId) {
        TypedQuery<Evaluation> query = em.createQuery(
                "SELECT e FROM Evaluation e WHERE e.evaluator.id = :evaluatorId", Evaluation.class);
        query.setParameter("evaluatorId", evaluatorId);
        return query.getResultList();
    }

    @Override
    public List<Evaluation> findByCycleId(Long cycleId) {
        TypedQuery<Evaluation> query = em.createQuery(
                "SELECT e FROM Evaluation e WHERE e.evaluationCycle.id = :cycleId", Evaluation.class);
        query.setParameter("cycleId", cycleId);
        return query.getResultList();
    }

    @Override
    public List<Evaluation> findCompletedEvaluations() {
        return executeNamedQuery("Evaluation.findCompleted", Evaluation.class);
    }

    @Override
    public List<Evaluation> findByDateRange(Instant startDate, Instant endDate) {
        TypedQuery<Evaluation> query = em.createQuery(
                "SELECT e FROM Evaluation e WHERE e.createdAt BETWEEN :startDate AND :endDate", Evaluation.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    @Transactional
    public int closeAllCompletedEvaluations() {
        return em.createQuery(
                "UPDATE Evaluation e SET e.status = 'CLOSED' WHERE e.status = 'COMPLETED'")
                .executeUpdate();
    }

    @Override
    public List<Evaluation> getEvaluationsByFilters(EvaluationFilter filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Evaluation> query = cb.createQuery(Evaluation.class);
        Root<Evaluation> root = query.from(Evaluation.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filters.getEvaluationId() != null) {
            predicates.add(cb.equal(root.get("id"), filters.getEvaluationId()));
        }
        if (filters.getEvaluatedId() != null) {
            predicates.add(cb.equal(root.get("evaluated").get("id"), filters.getEvaluatedId()));
        }
        if (filters.getEvaluatorId() != null) {
            predicates.add(cb.equal(root.get("evaluator").get("id"), filters.getEvaluatorId()));
        }
        if (filters.getEvaluationStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filters.getEvaluationStatus()));
        }
        if (filters.getEvaluationScale() != null) {
            predicates.add(cb.equal(root.get("scale"), filters.getEvaluationScale()));
        }
        if (filters.getDateRange() != null) {
            predicates.add(cb.between(root.get("createdAt"),
                    filters.getDateRange().getStartDate().atStartOfDay(),
                    filters.getDateRange().getEndDate().atTime(23, 59, 59)));
        }
        if (filters.getSearchText() != null && !filters.getSearchText().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("feedback")), "%" + filters.getSearchText().toLowerCase() + "%"));
        }

        query.where(cb.and(predicates.toArray(Predicate[]::new))); // TODO Ver nota ao fundo da classe

        if (filters.getEvaluationSortField() != null) {
            if (Boolean.TRUE.equals(filters.getAscending())) {
                query.orderBy(cb.asc(root.get(filters.getEvaluationSortField().getFieldName())));
            } else {
                query.orderBy(cb.desc(root.get(filters.getEvaluationSortField().getFieldName())));
            }
        }

        return em.createQuery(query).getResultList();
    }

}

/**
 * TODO esta mensagem é para autodestruir em 10 segundos :)
 * 
 * Why change this:
 * query.where(cb.and(predicates.toArray(new Predicate[0])));
 * to this:
 * query.where(cb.and(predicates.toArray(Predicate[]::new)));
 * ?
 * # Array Creation in Java: Comparing Methods
 * 
 * Both expressions achieve the same result - they convert a List of Predicates
 * to a Predicate array. However, they use different approaches:
 * 
 * ## Traditional Approach
 * ```java
 * predicates.toArray(new Predicate[0])
 * ```
 * 
 * ## Method Reference Approach (Java 8+)
 * ```java
 * predicates.toArray(Predicate[]::new)
 * ```
 * 
 * ## Why Make This Change?
 * 
 * 1. **Modern syntax**: The method reference syntax (`Predicate[]::new`) is a
 * more modern Java feature introduced in Java 8.
 * 
 * 2. **Performance considerations**: The method reference version is
 * potentially more efficient. When you use `new Predicate[0]`, the JVM creates
 * an empty array first, then the `toArray` method must allocate a new array of
 * the correct size. With the method reference, the JVM can directly create an
 * array of the appropriate size.
 * 
 * 3. **Readability**: For developers familiar with Java 8+ features, the method
 * reference style is considered cleaner and more concise.
 * 
 * 4. **Consistency**: If the rest of your codebase uses modern Java idioms,
 * this change keeps the style consistent.
 * 
 * The change is mostly stylistic and follows modern Java best practices. Both
 * versions are functionally equivalent, but the method reference version is
 * generally preferred in contemporary Java code.
 */