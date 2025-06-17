package aor.proj3t.util.converterutils;

import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Converter for Evaluation entities and DTOs.
 * 
 * This class handles the bidirectional conversion between {@link Evaluation}
 * entities and
 * {@link EvaluationDTO} data transfer objects. Evaluations store performance
 * assessment data
 * including ratings, feedback, and relationships between evaluators and
 * evaluatees within
 * specific evaluation cycles.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class EvaluationConverter extends AbstractConverter<Evaluation, EvaluationDTO, Long> {
    @Override
    protected Class<Evaluation> getEntityClass() {
        return Evaluation.class;
    }

    @Override
    protected Evaluation createEntityInstance() {
        return new Evaluation();
    }

    @Override
    protected EvaluationDTO createDTOInstance() {
        return new EvaluationDTO();
    }

    @Override
    protected Long getIdFromDTO(EvaluationDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(Evaluation evaluation, EvaluationDTO dto) {
        dto.setId(evaluation.getId());
        dto.setScale(evaluation.getScale());
        dto.setStatus(evaluation.getStatus());
        dto.setFeedback(evaluation.getFeedback());
        dto.setCreatedAt(evaluation.getCreatedAt());
        dto.setEditedAt(evaluation.getEditedAt());

        if (evaluation.getEvaluationCycle() != null) {
            dto.setEvaluationCycleId(evaluation.getEvaluationCycle().getId());
        }

        if (evaluation.getEvaluated() != null) {
            dto.setEvaluateeId(evaluation.getEvaluated().getId());
        }

        if (evaluation.getEvaluator() != null) {
            dto.setEvaluatorId(evaluation.getEvaluator().getId());
        }
    }

    @Override
    protected void mapDTOToEntity(EvaluationDTO dto, Evaluation evaluation) {
        evaluation.setScale(dto.getScale());
        evaluation.setStatus(dto.getStatus());
        evaluation.setFeedback(dto.getFeedback());
        evaluation.setCreatedAt(dto.getCreatedAt());
        evaluation.setEditedAt(dto.getEditedAt());

        // Handle relationships using findSafely
        EvaluationCycle evaluationCycle = findSafely(EvaluationCycle.class, dto.getEvaluationCycleId());
        evaluation.setEvaluationCycle(evaluationCycle);

        User evaluatee = findSafely(User.class, dto.getEvaluateeId());
        evaluation.setEvaluated(evaluatee);

        User evaluator = findSafely(User.class, dto.getEvaluatorId());
        evaluation.setEvaluator(evaluator);
    }
}
