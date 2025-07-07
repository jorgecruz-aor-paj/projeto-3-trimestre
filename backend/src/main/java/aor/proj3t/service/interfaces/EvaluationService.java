package aor.proj3t.service.interfaces;

import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.filters.EvaluationFilter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface EvaluationService {

    /**
     * Create a new evaluation.
     * 
     * @param evaluatedUserId   The ID of the user being evaluated
     * @param evaluatorUserId   The ID of the evaluator
     * @param evaluationCycleId The ID of the evaluation cycle
     * @return The created evaluation
     */
    Boolean createEvaluation(Long evaluatedUserId, Long evaluatorUserId, Long evaluationCycleId);

    /**
     * Find an evaluation by ID.
     * 
     * @param evaluationId The ID of the evaluation
     * @return An Optional containing the evaluation if found, or empty if not found
     */
    EvaluationDTO getEvaluationById(Long evaluationId);

    /**
     * Update an evaluation.
     * 
     * @param requesterId The ID of requester
     * @param evaluationDTO The evaluationDto to update the DataBase
     */
    EvaluationDTO updateEvaluation(Long requesterId, EvaluationDTO evaluationDTO);

    /**
     * Find evaluations for a specific evaluator.
     * 
     * @param evaluatorId The ID of the evaluator
     * @return A list of evaluations for the specified evaluator
     */
    List<EvaluationDTO> findEvaluationsByEvaluator(Long evaluatorId);

    /**
     * Find evaluations for a specific evaluated user.
     * 
     * @param evaluatedId The ID of the evaluated user
     * @return A list of evaluations for the specified evaluated user
     */
    List<EvaluationDTO> findEvaluationsByEvaluated(Long evaluatedId);

    /**
     * Find evaluations for a specific cycle.
     * 
     * @param cycleId The ID of the evaluation cycle
     * @return A list of evaluations for the specified cycle
     */
    List<EvaluationDTO> findEvaluationsByCycle(Long cycleId);

    /**
     * Find evaluations within a date range.
     * 
     * @param startDate The start date
     * @param endDate   The end date
     * @return A list of evaluations created within the specified date range
     */
    List<EvaluationDTO> findEvaluationsByDateRange(Instant startDate, Instant endDate);

    /**
     * Complete an evaluation.
     * 
     * @param evaluationId The ID of the evaluation
     * @return The updated evaluation
     */
    Boolean completeEvaluation(Long evaluationId);

    Boolean closeEvaluation (Long evaluationId);

    void closeEvaluationsBulk (Long requesterId, EvaluationCloseDTO evaluationCloseDto);

    /**
     * Close all completed evaluations.
     * 
     * @return The number of evaluations that were closed
     */
    Integer closeAllCompletedEvaluations();

    List<EvaluationDTO> getEvaluationsByFilter(Long requesterId, EvaluationFilter filter);

    Map<String, Long> getEvaluationsCompletedStats(Long requesterId);

   }
