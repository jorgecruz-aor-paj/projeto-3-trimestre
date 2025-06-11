package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import aor.proj3t.daos.interfaces.EvaluationDAO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.enums.EvaluationStatus;
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
    protected Class<Evaluation> getEntityClass() {
        return Evaluation.class;
    }

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

        query.where(cb.and(predicates.toArray(Predicate[]::new)));

        if (filters.getEvaluationSortField() != null) {
            if (Boolean.TRUE.equals(filters.getAscending())) {
                query.orderBy(cb.asc(root.get(filters.getEvaluationSortField().getFieldName())));
            } else {
                query.orderBy(cb.desc(root.get(filters.getEvaluationSortField().getFieldName())));
            }
        }

        return em.createQuery(query).getResultList();
    }

    @Override
    public Integer countUnclosedEvaluationsByCycleId(Long cycleId) {
        Long count = em.createQuery(
                "SELECT COUNT(e) FROM Evaluation e WHERE e.evaluationCycle.id = :cycleId AND e.status <> :closedStatus",
                Long.class)
                .setParameter("cycleId", cycleId)
                .setParameter("closedStatus", EvaluationStatus.CLOSED)
                .getSingleResult();
        return count.intValue();
    }
}
