package aor.proj3t.service.interfaces;

import java.util.List;
import java.util.Optional;

import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationStatus;

public interface CycleService {

    /**
     * Start a new evaluation cycle.
     * 
     * @param initiatedById The ID of the user who initiated the cycle
     * @return The created evaluation cycle
     */
    EvaluationCycle startCycle(Long initiatedById);

    /**
     * Find an evaluation cycle by ID.
     * 
     * @param cycleId The ID of the cycle
     * @return An Optional containing the cycle if found, or empty if not found
     */
    Optional<EvaluationCycle> findCycleById(Long cycleId);

    /**
     * Find the active evaluation cycle if exists.
     * 
     * @return An Optional containing the active cycle if found, or empty if not
     *         found
     */
    Optional<EvaluationCycle> findActiveCycle();

    /**
     * Find all evaluation cycles.
     * 
     * @return A list of all evaluation cycles
     */
    List<EvaluationCycle> findAllCycles();

    /**
     * Update the status of an evaluation cycle.
     * 
     * @param cycleId The ID of the cycle
     * @param status  The new status
     * @return The updated cycle
     */
    EvaluationCycle updateCycleStatus(Long cycleId, EvaluationStatus status);
}
