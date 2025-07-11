package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationCycleStatus;

/**
 * Data Access Object interface for EvaluationCycle entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide specific operations
 * for managing evaluation cycles in the performance evaluation system.
 * Evaluation cycles represent time periods during which employee evaluations
 * are conducted and managed.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Managing active evaluation cycles</li>
 * <li>Status updates for evaluation cycles</li>
 * <li>Counting and filtering operations</li>
 * <li>Optional evaluation data loading for performance optimization</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see EvaluationCycle
 * @see EvaluationCycleStatus
 * @see BasicDAO
 */
public interface EvaluationCycleDAO extends BasicDAO<EvaluationCycle, Long> {

    /**
     * Retrieves all active evaluation cycles from the data store.
     * <p>
     * An active evaluation cycle is one that has a status allowing for ongoing
     * evaluation activities. This method provides flexibility to optionally
     * load associated evaluation data for performance optimization.
     * </p>
     *
     * @param getEvaluations {@code true} to fetch associated evaluation entities,
     *                       {@code false} to exclude them for better performance
     * @return A {@link List} of active evaluation cycles, or an empty list if none
     *         exist
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<EvaluationCycle> findActive(boolean getEvaluations);

    /**
     * Updates the status of a specific evaluation cycle.
     * <p>
     * This method allows for transitioning evaluation cycles between different
     * states (e.g., from ACTIVE to CLOSED, or from DRAFT to ACTIVE).
     * Status updates may trigger business logic or notifications in the
     * application.
     * </p>
     *
     * @param cycleId The unique identifier of the evaluation cycle to update. Must
     *                not be null.
     * @param status  The new status to set for the evaluation cycle. Must not be
     *                null.
     * @throws IllegalArgumentException                  if cycleId or status is
     *                                                   null
     * @throws javax.persistence.EntityNotFoundException if no evaluation cycle with
     *                                                   the given ID exists
     * @throws javax.persistence.PersistenceException    if a database access error
     *                                                   occurs
     * @see EvaluationCycleStatus
     */
    void updateStatus(Long cycleId, EvaluationCycleStatus status);

    /**
     * Counts the total number of active evaluation cycles.
     * <p>
     * This method provides a quick count of evaluation cycles that are currently
     * active in the system. Useful for dashboard displays and system monitoring.
     * </p>
     *
     * @return The count of active evaluation cycles, or 0 if none exist
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Long countActiveEvaluationCycles();

    /**
     * Retrieves all evaluation cycles with optional evaluation data loading.
     * <p>
     * This method provides flexibility to fetch all evaluation cycles while
     * controlling whether associated evaluation entities should be loaded.
     * Use {@code loadEvaluations = false} for better performance when evaluation
     * details are not needed.
     * </p>
     *
     * @param loadEvaluations {@code true} to fetch associated evaluation entities,
     *                        {@code false} to exclude them for better performance
     * @return A {@link List} of all evaluation cycles, or an empty list if none
     *         exist
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<EvaluationCycle> findAll(boolean loadEvaluations);

}
