package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.filters.EvaluationFilter;

import java.time.Instant;
import java.util.List;

/**
 * Repository interface for Evaluation entity operations.
 */
public interface EvaluationDAO extends BasicDAO<Evaluation, Long> {


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
     * Find evaluations within a specific date range.
     *
     * @param startDate The start date of the range
     * @param endDate   The end date of the range
     * @return A list of evaluations created within the specified date range
     */
    List<Evaluation> findByDateRange(Instant startDate, Instant endDate);

    /**
     * Close all completed evaluations.
     *
     * @return The number of evaluations that were closed
     */
    int closeAllCompletedEvaluations();

    List<Evaluation> getEvaluationsByFilters(EvaluationFilter filters);

    Integer countUnclosedEvaluationsByCycleId(Long cycleId);

    Long countOngoingEvaluationsByCycle(Long cycleId);

    Long countByCycleId(Long cycleId);

    List<Evaluation> findByEvaluatedAndStatusIn(User evaluated, List<EvaluationStatus> statuses);

}
