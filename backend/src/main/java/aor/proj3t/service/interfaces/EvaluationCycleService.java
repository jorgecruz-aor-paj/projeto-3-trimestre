package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.filters.DateRange;

/**
 * Service interface for evaluation cycle management operations.
 * This interface defines the contract for managing the complete lifecycle of
 * evaluation cycles
 * including creation, activation, closure, and user notification workflows.
 * 
 * <p>
 * Key evaluation cycle services include:
 * <ul>
 * <li><b>Cycle Creation:</b> Starting new evaluation periods with date
 * validation</li>
 * <li><b>Bulk Evaluation Generation:</b> Creating evaluations for all managed
 * users</li>
 * <li><b>Cycle Management:</b> Retrieving active and historical evaluation
 * cycles</li>
 * <li><b>Notification Workflows:</b> Email notifications for cycle events and
 * closures</li>
 * <li><b>Validation Controls:</b> Ensuring all users have managers before
 * starting cycles</li>
 * <li><b>Integration Support:</b> Asynchronous email service integration</li>
 * </ul>
 * 
 * <p>
 * This service ensures proper evaluation cycle management by validating
 * organizational
 * readiness, creating comprehensive evaluation coverage, and managing the
 * complete cycle workflow.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
public interface EvaluationCycleService {

    /**
     * Starts a new evaluation cycle for the specified date range.
     * Validates that all users have assigned managers, creates the evaluation
     * cycle,
     * generates evaluations for all eligible users, and sends notification emails.
     * 
     * @param adminId   the ID of the admin user starting the evaluation cycle
     * @param dateRange the date range defining the evaluation period
     * @throws ForbiddenException   if the requester is not an admin
     * @throws InvalidDataException if there are users without assigned managers
     * @throws ConflictException    if there's an overlapping evaluation cycle
     */
    void startCycle(Long adminId, DateRange dateRange);

    /**
     * Retrieves the currently active evaluation cycle.
     * Returns the evaluation cycle that is currently in progress.
     * 
     * @param requesterId the ID of the user requesting the active cycle information
     * @return EvaluationCycleDTO representing the active cycle, or null if no
     *         active cycle
     * @throws ForbiddenException if the requester lacks permission to view cycle
     *                            information
     */
    EvaluationCycleDTO getActiveCycle(Long requesterId);

    /**
     * Retrieves evaluation cycles based on their active status.
     * Can return either active cycles only or all historical cycles.
     * 
     * @param requesterId the ID of the user requesting cycle information
     * @param active      true to retrieve only active cycles, false to retrieve all
     *                    cycles
     * @return list of EvaluationCycleDTO objects matching the active status
     *         criteria
     * @throws ForbiddenException if the requester lacks permission to view cycle
     *                            information
     */
    List<EvaluationCycleDTO> getAllCycles(Long requesterId, boolean active);

    /**
     * Sends evaluation cycle closure notification emails to all users.
     * Processes cycle closure and initiates email notification workflow.
     * 
     * @param cycle the EvaluationCycle entity that is being closed
     */
    void sendCycleClosureEmail(EvaluationCycle cycle);

    /**
     * Checks if an evaluation cycle should be closed based on the completion status
     * of its evaluations.
     * A cycle is closed when all evaluations within it are either completed or
     * closed.
     * When a cycle is closed, its status is updated and closure notifications are
     * sent.
     * 
     * @param evaluationCycleId the ID of the evaluation cycle to check
     */
    void checkAndCloseEvaluationCycle(Long evaluationCycleId);

    /**
     * Checks if an evaluation cycle should be marked as completed based on the
     * status of its evaluations.
     * A cycle is marked as completed when there are no ongoing evaluations.
     * When a cycle is completed, notifications are sent to the admin responsible
     * for the cycle.
     * 
     * @param evaluationCycleId the ID of the evaluation cycle to check
     */
    void checkAndCompleteEvaluationCycle(Long evaluationCycleId);
}
