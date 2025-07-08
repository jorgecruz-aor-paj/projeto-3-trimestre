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
     * Update the status of an evaluation cycle.
     *
     * @param cycleId The ID of the evaluation cycle
     * @param status  The new status
     */
    void updateStatus(Long cycleId, EvaluationCycleStatus status);

    Long countActiveEvaluationCycles();

    List<EvaluationCycle> findAll(boolean loadEvaluations);



}
