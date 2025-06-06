package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationStatus;

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
     * Retrieves a paginated list of EvaluationCycle entities filtered by status.
     *
     * <p>
     * This method applies pagination by using the provided page number and size.
     * It ensures efficient query execution and limits the number of results
     * returned.
     * </p>
     *
     * @param status The status used to filter evaluation cycles.
     * @param page   The page number (zero-based index) for pagination.
     * @param size   The number of records per page.
     * @return A list of EvaluationCycle entities matching the given status,
     *         limited to the specified page and size.
     */
    List<EvaluationCycle> findByStatus(EvaluationStatus status, int page, int size);

    /**
     * Update the status of an evaluation cycle.
     *
     * @param cycleId The ID of the evaluation cycle
     * @param status  The new status
     * @return true if the update was successful, false otherwise
     */
    boolean updateStatus(Long cycleId, EvaluationStatus status);
}
