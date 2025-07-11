package aor.proj3t.service.interfaces;

import java.util.List;
import java.util.Map;

import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.filters.EvaluationFilter;

/**
 * Service interface for evaluation management operations.
 * This interface defines the contract for comprehensive evaluation lifecycle
 * management
 * including creation, updates, approval workflows, and AI-powered sentiment
 * analysis.
 * 
 * <p>
 * Key evaluation services include:
 * <ul>
 * <li><b>Evaluation Lifecycle:</b> Creation, updates, and status
 * management</li>
 * <li><b>Multi-stage Approval:</b> Self-evaluation, manager feedback, and admin
 * approval workflows</li>
 * <li><b>AI Integration:</b> Sentiment analysis for feedback text and
 * evaluation insights</li>
 * <li><b>Bulk Operations:</b> Mass evaluation closure and status updates</li>
 * <li><b>Advanced Filtering:</b> Complex evaluation search and filtering
 * capabilities</li>
 * <li><b>Analytics & Reporting:</b> Evaluation completion statistics and
 * performance insights</li>
 * </ul>
 * 
 * <p>
 * This service ensures proper evaluation workflow progression with
 * authorization checks,
 * maintains data integrity, and provides comprehensive evaluation tracking and
 * reporting capabilities.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
public interface EvaluationService {

    /**
     * Updates an evaluation with new information and handles workflow progression.
     * Validates permissions, updates evaluation data, and manages status
     * transitions
     * through the evaluation workflow (self-evaluation, manager feedback,
     * approval).
     * 
     * @param requesterId   the ID of the user making the update request
     * @param evaluationDTO the evaluation data containing updated information
     * @return the updated EvaluationDTO with current status and data
     * @throws ForbiddenException if the requester lacks permission to update the
     *                            evaluation
     * @throws NotFoundException  if the evaluation doesn't exist
     */
    EvaluationDTO updateEvaluation(Long requesterId, EvaluationDTO evaluationDTO);

    /**
     * Closes multiple evaluations in a bulk operation with final approval or
     * rejection.
     * Handles mass evaluation closure with status updates and notification
     * workflows.
     * 
     * @param requesterId        the ID of the admin user performing the bulk
     *                           closure
     * @param evaluationCloseDto the data containing evaluation IDs and closure
     *                           information
     * @throws ForbiddenException if the requester is not authorized for bulk
     *                            operations
     */
    void closeEvaluationsBulk(Long requesterId, EvaluationCloseDTO evaluationCloseDto);

    /**
     * Retrieves evaluations based on specified filtering criteria.
     * Supports complex filtering by status, date ranges, users, and other
     * evaluation attributes.
     * 
     * @param requesterId the ID of the user making the request (used for
     *                    authorization)
     * @param filter      the filter criteria for evaluation search
     * @return a list of EvaluationDTO objects matching the filter criteria
     * @throws ForbiddenException if the requester lacks permission to view certain
     *                            evaluations
     */
    List<EvaluationDTO> getEvaluationsByFilter(Long requesterId, EvaluationFilter filter);

    /**
     * Retrieves evaluation completion statistics and metrics.
     * Provides insights into evaluation completion rates, pending evaluations,
     * and overall evaluation system performance.
     * 
     * @param requesterId the ID of the user requesting statistics (typically admin)
     * @return a map containing various evaluation completion statistics and counts
     * @throws ForbiddenException if the requester is not authorized to view
     *                            statistics
     */
    Map<String, Long> getEvaluationsCompletedStats(Long requesterId);

}
