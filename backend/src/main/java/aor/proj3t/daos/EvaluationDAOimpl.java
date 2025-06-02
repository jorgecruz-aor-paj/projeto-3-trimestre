package aor.proj3t.daos;


import aor.proj3t.entities.Evaluation;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the EvaluationRepository interface.
 */
@Stateless
public class EvaluationDAOimpl extends AbstractDAO<Evaluation, Long> implements EvaluationDAO {

    @Override
    public List<Evaluation> findByStatus(EvaluationStatus status) {
        return executeNamedQuery("Evaluation.findByStatus", "status", status);
    }

    @Override
    public List<Evaluation> findByEvaluated(Long evaluatedId) {
        return executeNamedQuery("Evaluation.findByEvaluated", "evaluatedId", evaluatedId);
    }

    @Override
    public List<Evaluation> findByEvaluator(Long evaluatorId) {
        return executeNamedQuery("Evaluation.findByEvaluator", "evaluatorId", evaluatorId);
    }

    @Override
    public List<Evaluation> findByCycleId(Long cycleId) {
        return executeNamedQuery("Evaluation.findByCycleId", "cycleId", cycleId);
    }

    @Override
    public List<Evaluation> findCompletedEvaluations() {
        return executeNamedQuery("Evaluation.findCompletedEvaluations");
    }

    @Override
    public List<Evaluation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<Evaluation> query = em.createQuery(
                "SELECT e FROM Evaluation e WHERE e.createdAt BETWEEN :startDate AND :endDate", Evaluation.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    public int closeAllCompletedEvaluations() {
        Query query = em.createQuery(
                "UPDATE Evaluation e SET e.status = :closedStatus WHERE e.status = :completedStatus");
        query.setParameter("closedStatus", EvaluationStatus.CLOSED);
        query.setParameter("completedStatus", EvaluationStatus.COMPLETED);
        return query.executeUpdate();
    }

    @Override
    public Optional<Evaluation> findByEvaluatedEvaluatorAndCycle(Long evaluatedId, Long evaluatorId, Long cycleId) {
        try {
            TypedQuery<Evaluation> query = em.createQuery(
                    "SELECT e FROM Evaluation e WHERE e.evaluatedUser.id = :evaluatedId " +
                            "AND e.evaluator.id = :evaluatorId AND e.evaluationCycle.id = :cycleId",
                    Evaluation.class);
            query.setParameter("evaluatedId", evaluatedId);
            query.setParameter("evaluatorId", evaluatorId);
            query.setParameter("cycleId", cycleId);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
