package aor.proj3t.DAOs;


import aor.proj3t.Entities.Evaluation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Evaluation entity operations.
 */
public interface EvaluationDAO extends BasicDAO <Evaluation, Long> {

    /**
     * Find evaluations by their status.
     *
     * @param status The evaluation status
     * @return A list of evaluations with the specified status
     */
    List<Evaluation> findByStatus(EvaluationStatus status);

    /**
     * Find evaluations for a specific evaluated user.
     *
     * @param evaluatedId The ID of the evaluated user
     * @return A list of evaluations for the specified user
     */
    List<Evaluation> findByEvaluated(Long evaluatedId);

    /**
     * Find evaluations performed by a specific evaluator.
     *
     * @param evaluatorId The ID of the evaluator
     * @return A list of evaluations performed by the specified evaluator
     */
    List<Evaluation> findByEvaluator(Long evaluatorId);

    /**
     * Find evaluations for a specific evaluation cycle.
     *
     * @param cycleId The ID of the evaluation cycle
     * @return A list of evaluations for the specified cycle
     */
    List<Evaluation> findByCycleId(Long cycleId);

    /**
     * Find all evaluations with "Completed" status.
     *
     * @return A list of completed evaluations
     */
    List<Evaluation> findCompletedEvaluations();

    /**
     * Find evaluations within a specific date range.
     *
     * @param startDate The start date of the range
     * @param endDate   The end date of the range
     * @return A list of evaluations created within the specified date range
     */
    List<Evaluation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Close all completed evaluations.
     *
     * @return The number of evaluations that were closed
     */
    int closeAllCompletedEvaluations();

    /**
     * Find an evaluation by evaluated user, evaluator and cycle.
     *
     * @param evaluatedId The ID of the evaluated user
     * @param evaluatorId The ID of the evaluator
     * @param cycleId     The ID of the evaluation cycle
     * @return An Optional containing the evaluation if found, or empty if not found
     */
    Optional<Evaluation> findByEvaluatedEvaluatorAndCycle(Long evaluatedId, Long evaluatorId, Long cycleId);
}
