package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import aor.proj3t.daos.interfaces.EvaluationDAO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.filters.EvaluationFilter;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

/**
 * Stateless EJB implementation of the {@link EvaluationDAO} interface.
 * <p>
 * This class provides concrete implementation for managing evaluation records
 * within the performance management system. It extends {@link AbstractDAO} to
 * leverage common CRUD operations and implements specialized functionality for
 * evaluation lifecycle management, status tracking, and complex business
 * queries.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Multi-dimensional evaluation filtering by cycle, evaluator, and
 * participant</li>
 * <li>Status-based evaluation lifecycle management and workflow control</li>
 * <li>Complex JPQL queries for evaluation analytics and reporting</li>
 * <li>Criteria API implementation for dynamic filtering capabilities</li>
 * <li>Bulk operations for status updates and evaluation processing</li>
 * <li>Date range querying for temporal evaluation analysis</li>
 * <li>Relationship traversal across evaluation cycles and user hierarchies</li>
 * </ul>
 * <p>
 * <strong>Transaction Management:</strong>
 * Critical operations utilize {@code @Transactional} annotations to ensure
 * data consistency during multi-step evaluation processes and status
 * transitions.
 * </p>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see EvaluationDAO
 * @see AbstractDAO
 * @see Evaluation
 * @see EvaluationFilter
 * @see EvaluationCycle
 */
@Stateless
public class EvaluationDAOimpl extends AbstractDAO<Evaluation, Long> implements EvaluationDAO {

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * This method implements concrete entity type resolution by returning the
     * Evaluation class reference. This enables the AbstractDAO parent class to
     * perform type-safe CRUD operations on Evaluation entities.
     * </p>
     *
     * @return the Evaluation.class for entity type resolution
     */
    @Override
    protected Class<Evaluation> getEntityClass() {
        return Evaluation.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Executes a straightforward JPQL query to retrieve all evaluations
     * associated with a specific evaluator. This method supports evaluator
     * workload analysis and performance tracking by providing access to
     * all evaluations conducted by a particular user.
     * </p>
     */
    @Override
    public List<Evaluation> findByEvaluator(Long evaluatorId) {
        TypedQuery<Evaluation> query = em.createQuery(
                "SELECT e FROM Evaluation e WHERE e.evaluator.id = :evaluatorId", Evaluation.class);
        query.setParameter("evaluatorId", evaluatorId);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Retrieves all evaluations belonging to a specific evaluation cycle using
     * JPQL entity relationship traversal. This method is essential for cycle
     * management, progress tracking, and bulk evaluation operations within
     * the performance management workflow.
     * </p>
     */
    @Override
    public List<Evaluation> findByCycleId(Long cycleId) {
        TypedQuery<Evaluation> query = em.createQuery(
                "SELECT e FROM Evaluation e WHERE e.evaluationCycle.id = :cycleId", Evaluation.class);
        query.setParameter("cycleId", cycleId);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Implements temporal filtering using JPQL BETWEEN clause to identify
     * evaluations created within a specific time range. This method supports
     * historical analysis, reporting, and time-based evaluation analytics
     * across the performance management system.
     * </p>
     */
    @Override
    public List<Evaluation> findByDateRange(Instant startDate, Instant endDate) {
        TypedQuery<Evaluation> query = em.createQuery(
                "SELECT e FROM Evaluation e WHERE e.createdAt BETWEEN :startDate AND :endDate", Evaluation.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Executes a bulk update operation to transition all evaluations with
     * COMPLETED status to CLOSED status. This method supports evaluation
     * lifecycle management and cycle finalization processes. The transactional
     * annotation ensures atomicity of the bulk operation.
     * </p>
     * <p>
     * <strong>Performance note:</strong> Uses JPQL UPDATE for efficient bulk
     * processing without requiring individual entity loading and modification.
     * </p>
     */
    @Override
    @Transactional
    public int closeAllCompletedEvaluations() {
        return em.createQuery(
                "UPDATE Evaluation e SET e.status = 'CLOSED' WHERE e.status = 'COMPLETED'")
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Implements comprehensive multi-criteria filtering using JPA Criteria API
     * with sophisticated relationship traversal and dynamic query construction.
     * This method provides extensive filtering capabilities including:
     * </p>
     * <ul>
     * <li><strong>Entity filtering:</strong> By evaluation ID and participant
     * relationships</li>
     * <li><strong>User filtering:</strong> Evaluator and evaluated user
     * identification</li>
     * <li><strong>Cycle integration:</strong> Evaluation cycle relationship
     * handling</li>
     * <li><strong>Status management:</strong> Evaluation and cycle status
     * filtering</li>
     * <li><strong>Temporal filtering:</strong> Date range evaluation selection</li>
     * <li><strong>Dynamic sorting:</strong> Configurable field and direction-based
     * ordering</li>
     * </ul>
     * <p>
     * The method utilizes INNER JOIN operations for evaluation cycle relationships
     * to ensure data consistency and enable cycle-based filtering operations.
     * Predicates are constructed dynamically based on non-null filter values,
     * optimizing query performance by including only necessary conditions.
     * </p>
     * <p>
     * <strong>Performance considerations:</strong>
     * </p>
     * <ul>
     * <li>INNER JOIN operations optimize relationship traversal</li>
     * <li>Predicate lists are pre-allocated for efficient memory usage</li>
     * <li>Conditional query building reduces unnecessary database load</li>
     * <li>Enum comparisons handle status filtering with type safety</li>
     * </ul>
     */
    @Override
    public List<Evaluation> getEvaluationsByFilters(EvaluationFilter filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Evaluation> query = cb.createQuery(Evaluation.class);
        Root<Evaluation> root = query.from(Evaluation.class);
        Join<Evaluation, EvaluationCycle> cycleJoin = root.join("evaluationCycle", JoinType.INNER);

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
            Instant start = filters.getDateRange().getStartDate();
            Instant end = filters.getDateRange().getEndDate();

            // Use EvaluationCycle.closureDate instead of Evaluation.createdAt
            predicates.add(cb.between(cycleJoin.get("closureDate"), start, end));
        }

        if (filters.getSearchText() != null && !filters.getSearchText().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("feedback")), "%" + filters.getSearchText().toLowerCase() + "%"));
        }

        if (filters.getCycleClosed() != null && filters.getCycleClosed()) {
            predicates.add(cb.equal(cycleJoin.get("status"), EvaluationCycleStatus.CLOSED));
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

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Executes a counting query to determine the number of evaluations within
     * a specific cycle that are not in CLOSED status. This method supports
     * cycle completion validation and progress tracking by identifying
     * outstanding evaluations that require attention.
     * </p>
     * <p>
     * Returns integer count for convenient business logic integration.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Executes a counting query to identify evaluations in ONGOING status
     * within a specific evaluation cycle. This method supports active
     * evaluation monitoring and workload management by tracking current
     * evaluation activities.
     * </p>
     */
    @Override
    public Long countOngoingEvaluationsByCycle(Long cycleId) {
        return em.createQuery(
                "SELECT COUNT(e) FROM Evaluation e WHERE e.evaluationCycle.id = :cycleId AND e.status = :status",
                Long.class)
                .setParameter("cycleId", cycleId)
                .setParameter("status", EvaluationStatus.ONGOING)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Provides a simple counting query to determine the total number of
     * evaluations associated with a specific evaluation cycle, regardless
     * of status. This method supports cycle analytics and capacity planning
     * by providing baseline evaluation metrics.
     * </p>
     */
    @Override
    public Long countByCycleId(Long cycleId) {
        return em.createQuery(
                "SELECT COUNT(e) FROM Evaluation e WHERE e.evaluationCycle.id = :cycleId",
                Long.class)
                .setParameter("cycleId", cycleId)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Utilizes JPA Criteria API to construct a dynamic query for finding
     * evaluations based on specific user and status combinations. This method
     * supports flexible evaluation lookup with multiple status filtering,
     * enabling complex business scenarios such as finding all active, pending,
     * or completed evaluations for a particular user.
     * </p>
     * <p>
     * Uses the IN clause for efficient multi-status filtering and entity
     * comparison for user-based lookup.
     * </p>
     */
    @Override
    public List<Evaluation> findByEvaluatedAndStatusIn(User evaluated, List<EvaluationStatus> statuses) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Evaluation> query = cb.createQuery(Evaluation.class);
        Root<Evaluation> root = query.from(Evaluation.class);

        Predicate byUser = cb.equal(root.get("evaluated"), evaluated);
        Predicate byStatus = root.get("status").in(statuses);

        query.where(cb.and(byUser, byStatus));

        return em.createQuery(query).getResultList();
    }

}
