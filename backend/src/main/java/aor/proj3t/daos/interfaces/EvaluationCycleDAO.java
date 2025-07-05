package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.EvaluationStatus;

import java.util.List;

/**
 * Repository interface for EvaluationCycle entity operations.
 */
public interface EvaluationCycleDAO extends BasicDAO<EvaluationCycle, Long> {

    /**
     * Find active evaluation cycles.
     * @Param boolean to fetch evaluation array or not
     * @return A list of active evaluation cycles
     */
    List<EvaluationCycle> findActive(boolean getEvaluations);

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
     */
    void updateStatus(Long cycleId, EvaluationCycleStatus status);

    Long countActiveEvaluationCycles();

    List<EvaluationCycle> findAll(boolean loadEvaluations);



}
