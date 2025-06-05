package aor.proj3t.service.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import aor.proj3t.entities.Evaluation;

public interface EvaluationService {

    /**
     * Create a new evaluation.
     * 
     * @param evaluatedUserId   The ID of the user being evaluated
     * @param evaluatorUserId   The ID of the evaluator
     * @param evaluationCycleId The ID of the evaluation cycle
     * @return The created evaluation
     */
    Evaluation createEvaluation(Long evaluatedUserId, Long evaluatorUserId, Long evaluationCycleId);

    /**
     * Find an evaluation by ID.
     * 
     * @param evaluationId The ID of the evaluation
     * @return An Optional containing the evaluation if found, or empty if not found
     */
    Optional<Evaluation> findEvaluationById(Long evaluationId);

    /**
     * Update an evaluation.
     * 
     * @param evaluationId The ID of the evaluation
     * @param score        The score
     * @param feedback     The feedback
     * @return The updated evaluation
     */
    Evaluation updateEvaluation(Long evaluationId, Integer score, String feedback);

    /**
     * Find evaluations for a specific evaluator.
     * 
     * @param evaluatorId The ID of the evaluator
     * @return A list of evaluations for the specified evaluator
     */
    List<Evaluation> findEvaluationsByEvaluator(Long evaluatorId);

    /**
     * Find evaluations for a specific evaluated user.
     * 
     * @param evaluatedId The ID of the evaluated user
     * @return A list of evaluations for the specified evaluated user
     */
    List<Evaluation> findEvaluationsByEvaluated(Long evaluatedId);

    /**
     * Find evaluations for a specific cycle.
     * 
     * @param cycleId The ID of the evaluation cycle
     * @return A list of evaluations for the specified cycle
     */
    List<Evaluation> findEvaluationsByCycle(Long cycleId);

    /**
     * Find evaluations within a date range.
     * 
     * @param startDate The start date
     * @param endDate   The end date
     * @return A list of evaluations created within the specified date range
     */
    List<Evaluation> findEvaluationsByDateRange(Instant startDate, Instant endDate);

    /**
     * Complete an evaluation.
     * 
     * @param evaluationId The ID of the evaluation
     * @return The updated evaluation
     */
    Evaluation completeEvaluation(Long evaluationId);

    /**
     * Close all completed evaluations.
     * 
     * @return The number of evaluations that were closed
     */
    int closeAllCompletedEvaluations();

    // Converto to DTO methods:
    //
    // /**
    // * Convert an Evaluation entity to an EvaluationDTO.
    // *
    // * @param evaluation The evaluation entity
    // * @return The evaluation DTO
    // */
    // EvaluationDTO convertToDTO(Evaluation evaluation);
    //
    // /**
    // * Convert an EvaluationCycle entity to an EvaluationCycleDTO.
    // *
    // * @param cycle The evaluation cycle entity
    // * @return The evaluation cycle DTO
    // */
    // EvaluationCycleDTO convertToDTO(EvaluationCycle cycle);
    //
}
