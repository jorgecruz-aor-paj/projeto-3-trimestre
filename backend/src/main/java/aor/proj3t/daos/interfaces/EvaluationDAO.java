package aor.proj3t.daos.interfaces;

import java.time.Instant;
import java.util.List;

import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.filters.EvaluationFilter;

/**
 * Data Access Object interface for Evaluation entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide comprehensive operations
 * for managing employee evaluations within the performance evaluation system.
 * Evaluations represent the assessment records between evaluators and employees
 * during specific evaluation cycles.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Querying evaluations by evaluator, cycle, and date ranges</li>
 * <li>Status-based filtering and counting operations</li>
 * <li>Bulk operations for closing completed evaluations</li>
 * <li>Advanced filtering using {@link EvaluationFilter}</li>
 * <li>Performance monitoring and reporting capabilities</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see Evaluation
 * @see EvaluationStatus
 * @see EvaluationFilter
 * @see BasicDAO
 */
public interface EvaluationDAO extends BasicDAO<Evaluation, Long> {

    /**
     * Retrieves all evaluations performed by a specific evaluator.
     * <p>
     * This method finds all evaluation records where the specified user
     * acts as the evaluator. Useful for generating evaluator workload
     * reports and performance tracking.
     * </p>
     *
     * @param evaluatorId The unique identifier of the evaluator. Must not be null.
     * @return A {@link List} of evaluations performed by the specified evaluator,
     *         or an empty list if no evaluations are found
     * @throws IllegalArgumentException               if evaluatorId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<Evaluation> findByEvaluator(Long evaluatorId);

    /**
     * Retrieves all evaluations belonging to a specific evaluation cycle.
     * <p>
     * This method finds all evaluation records that are part of the specified
     * evaluation cycle. Essential for cycle management and progress tracking.
     * </p>
     *
     * @param cycleId The unique identifier of the evaluation cycle. Must not be
     *                null.
     * @return A {@link List} of evaluations for the specified cycle,
     *         or an empty list if no evaluations are found
     * @throws IllegalArgumentException               if cycleId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<Evaluation> findByCycleId(Long cycleId);

    /**
     * Retrieves evaluations created within a specific date range.
     * <p>
     * This method finds all evaluations that were created between the specified
     * start and end dates (inclusive). Useful for reporting and analytics
     * over specific time periods.
     * </p>
     *
     * @param startDate The start date of the range (inclusive). Must not be null.
     * @param endDate   The end date of the range (inclusive). Must not be null and
     *                  must be after startDate.
     * @return A {@link List} of evaluations created within the specified date
     *         range,
     *         or an empty list if no evaluations are found
     * @throws IllegalArgumentException               if startDate or endDate is
     *                                                null, or if endDate is before
     *                                                startDate
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<Evaluation> findByDateRange(Instant startDate, Instant endDate);

    /**
     * Closes all evaluations that have been completed but not yet closed.
     * <p>
     * This bulk operation updates the status of all evaluations that are
     * in a completed state to closed. This is typically used during cycle
     * closure or administrative cleanup operations.
     * </p>
     *
     * @return The number of evaluations that were successfully closed
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    int closeAllCompletedEvaluations();

    /**
     * Retrieves evaluations based on complex filtering criteria.
     * <p>
     * This method provides advanced querying capabilities using a filter object
     * that can specify multiple criteria such as status, date ranges, evaluator,
     * and other evaluation properties.
     * </p>
     *
     * @param filters The filter criteria to apply. Must not be null.
     * @return A {@link List} of evaluations matching the filter criteria,
     *         or an empty list if no matches are found
     * @throws IllegalArgumentException               if filters is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     * @see EvaluationFilter
     */
    List<Evaluation> getEvaluationsByFilters(EvaluationFilter filters);

    /**
     * Counts unclosed evaluations within a specific evaluation cycle.
     * <p>
     * This method provides a count of evaluations that are not yet closed
     * within the specified cycle. Useful for tracking cycle completion progress.
     * </p>
     *
     * @param cycleId The unique identifier of the evaluation cycle. Must not be
     *                null.
     * @return The count of unclosed evaluations in the cycle, or 0 if none exist
     * @throws IllegalArgumentException               if cycleId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Integer countUnclosedEvaluationsByCycleId(Long cycleId);

    /**
     * Counts ongoing evaluations within a specific evaluation cycle.
     * <p>
     * This method provides a count of evaluations that are currently in progress
     * (not completed or closed) within the specified cycle. Essential for
     * monitoring active evaluation activities.
     * </p>
     *
     * @param cycleId The unique identifier of the evaluation cycle. Must not be
     *                null.
     * @return The count of ongoing evaluations in the cycle, or 0 if none exist
     * @throws IllegalArgumentException               if cycleId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Long countOngoingEvaluationsByCycle(Long cycleId);

    /**
     * Counts all evaluations within a specific evaluation cycle.
     * <p>
     * This method provides the total count of evaluations (regardless of status)
     * that belong to the specified cycle. Useful for cycle statistics and
     * reporting.
     * </p>
     *
     * @param cycleId The unique identifier of the evaluation cycle. Must not be
     *                null.
     * @return The total count of evaluations in the cycle, or 0 if none exist
     * @throws IllegalArgumentException               if cycleId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Long countByCycleId(Long cycleId);

    /**
     * Retrieves evaluations for a specific evaluated user filtered by status.
     * <p>
     * This method finds all evaluations where the specified user is being evaluated
     * and the evaluation has one of the specified statuses. Useful for employee
     * self-service portals and performance tracking.
     * </p>
     *
     * @param evaluated The user being evaluated. Must not be null.
     * @param statuses  The list of evaluation statuses to filter by. Must not be
     *                  null or empty.
     * @return A {@link List} of evaluations for the specified user with matching
     *         statuses,
     *         or an empty list if no matches are found
     * @throws IllegalArgumentException               if evaluated is null or
     *                                                statuses is null or empty
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     * @see EvaluationStatus
     */
    List<Evaluation> findByEvaluatedAndStatusIn(User evaluated, List<EvaluationStatus> statuses);

}
