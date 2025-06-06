package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class EvaluationConverter implements Converter<Evaluation, EvaluationDTO> {

    @Inject
    private EntityManager em;

    @Override
    public EvaluationDTO toDTO(Evaluation evaluation) {
        if (evaluation == null) {
            return null;
        }

        return new EvaluationDTO(
                evaluation.getId(),
                evaluation.getScale(),
                evaluation.getStatus(),
                evaluation.getFeedback(),
                evaluation.getCreatedAt(),
                evaluation.getEditedAt(),
                evaluation.getEvaluationCycle() != null ? evaluation.getEvaluationCycle().getId() : null,
                evaluation.getEvaluated() != null ? evaluation.getEvaluated().getId() : null,
                evaluation.getEvaluator() != null ? evaluation.getEvaluator().getId() : null);
    }

    @Override
    public Evaluation toEntity(EvaluationDTO dto) {
        if (dto == null) {
            return null;
        }

        Evaluation evaluation;
        if (dto.getId() != null && dto.getId() != 0) {
            evaluation = em.find(Evaluation.class, dto.getId());
            if (evaluation == null) {
                evaluation = new Evaluation();
            }
        } else {
            evaluation = new Evaluation();
        }

        evaluation.setScale(dto.getScale());
        evaluation.setStatus(dto.getState());
        evaluation.setFeedback(dto.getFeedback());
        evaluation.setCreatedAt(dto.getCreatedAt());
        evaluation.setEditedAt(dto.getEditedAt());

        // Handle relationships
        if (dto.getEvaluationCycleId() != null) {
            EvaluationCycle evaluationCycle = em.find(EvaluationCycle.class, dto.getEvaluationCycleId());
            evaluation.setEvaluationCycle(evaluationCycle);
        }

        if (dto.getEvaluateeId() != null) {
            User evaluatee = em.find(User.class, dto.getEvaluateeId());
            evaluation.setEvaluated(evaluatee);
        }

        if (dto.getEvaluatorId() != null) {
            User evaluator = em.find(User.class, dto.getEvaluatorId());
            evaluation.setEvaluator(evaluator);
        }

        return evaluation;
    }
}
