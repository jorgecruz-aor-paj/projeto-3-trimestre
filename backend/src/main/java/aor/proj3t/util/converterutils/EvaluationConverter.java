package aor.proj3t.util.converterutils;

import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

    @Inject
    MiniEvaluationCycleConverter miniEvaluationCycleConverter;

    @Override
    public Class<Evaluation> getEntityClass() {
        return Evaluation.class;
    }

    @Override
    public Evaluation createEntityInstance() {
        return new Evaluation();
    }

    @Override
    public EvaluationDTO createDTOInstance() {
        return new EvaluationDTO();
    }

    @Override
    public Long getIdFromDTO(EvaluationDTO dto) {
        return dto.getId();
    }

    @Override
    public void mapEntityToDTO(Evaluation evaluation, EvaluationDTO dto) {
        dto.setId(evaluation.getId());
        dto.setScale(evaluation.getScale());
        dto.setStatus(evaluation.getStatus());
        dto.setFeedback(evaluation.getFeedback());
        dto.setCreatedAt(evaluation.getCreatedAt());
        dto.setEditedAt(evaluation.getEditedAt());

        if (evaluation.getEvaluationCycle() != null) {
            dto.setEvaluationCycle( miniEvaluationCycleConverter.toMiniEvaluationCycleDTO(evaluation.getEvaluationCycle()));
        }

        if (evaluation.getEvaluated() != null) {
            MiniUserDTO miniUserDTO = new MiniUserDTO(
                    evaluation.getEvaluated().getId(),
                    evaluation.getEvaluated().getFirstName(),
                    evaluation.getEvaluated().getLastName(),
                    evaluation.getEvaluated().getEmail(),
                    evaluation.getEvaluated().getPhotograph());
            dto.setEvaluatee(miniUserDTO);
        }

        if (evaluation.getEvaluator() != null) {
            MiniUserDTO miniUserDTO = new MiniUserDTO(
                    evaluation.getEvaluator().getId(),
                    evaluation.getEvaluator().getFirstName(),
                    evaluation.getEvaluator().getLastName(),
                    evaluation.getEvaluator().getEmail(),
                    evaluation.getEvaluator().getPhotograph());
            dto.setEvaluator(miniUserDTO);
        }
    }

    @Override
    public void mapDTOToEntity(EvaluationDTO dto, Evaluation evaluation) {
        evaluation.setScale(dto.getScale());
        evaluation.setStatus(dto.getStatus());
        evaluation.setFeedback(dto.getFeedback());
        evaluation.setCreatedAt(dto.getCreatedAt());
        evaluation.setEditedAt(dto.getEditedAt());

        // Handle relationships using findSafely
        EvaluationCycle evaluationCycle = findSafely(EvaluationCycle.class, dto.getEvaluationCycle() != null ? dto.getEvaluationCycle().getId() : null);
        evaluation.setEvaluationCycle(evaluationCycle);

        User evaluatee = findSafely(User.class, dto.getEvaluatee() != null ? dto.getEvaluatee().getId() : null);
        evaluation.setEvaluated(evaluatee);

        User evaluator = findSafely(User.class, dto.getEvaluator() != null ? dto.getEvaluator().getId() : null);
        evaluation.setEvaluator(evaluator);
    }
}
