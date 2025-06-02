package aor.proj3t.daos;


import aor.proj3t.entities.EvaluationCycle;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EvaluationCycle entity operations.
 */
public interface EvaluationCycleDAO extends BasicDAO<EvaluationCycle, Long> {

    /**
     * Find active evaluation cycles.
     *
     * @return A list of active evaluation cycles
     */
    List<EvaluationCycle> findActive();

    /**
     * Find evaluation cycles by their status.
     *
     * @param status The evaluation cycle status
     * @return A list of evaluation cycles with the specified status
     */
    List<EvaluationCycle> findByStatus(EvaluationStatus status);

    /**
     * Find the latest evaluation cycle.
     *
     * @return An Optional containing the latest evaluation cycle if one exists, or
     *         empty if none exists
     */
    Optional<EvaluationCycle> findLatest();

    /**
     * Update the status of an evaluation cycle.
     *
     * @param cycleId The ID of the evaluation cycle
     * @param status  The new status
     * @return true if the update was successful, false otherwise
     */
    boolean updateStatus(Long cycleId, EvaluationStatus status);
}
