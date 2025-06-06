package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class EvaluationCycleConverter implements Converter<EvaluationCycle, EvaluationCycleDTO> {

    @Inject
    private EntityManager em;

    @Override
    public EvaluationCycleDTO toDTO(EvaluationCycle evaluationCycle) {
        if (evaluationCycle == null) {
            return null;
        }

        return new EvaluationCycleDTO(
                evaluationCycle.getId(),
                evaluationCycle.getCreatedAt(),
                evaluationCycle.getStartDate(),
                evaluationCycle.getEndDate(),
                evaluationCycle.getClosureDate(),
                evaluationCycle.getStatus(),
                evaluationCycle.getCreatedBy() != null ? evaluationCycle.getCreatedBy().getId() : null);
    }

    @Override
    public EvaluationCycle toEntity(EvaluationCycleDTO dto) {
        if (dto == null) {
            return null;
        }

        EvaluationCycle evaluationCycle;
        if (dto.getId() != null && dto.getId() != 0) {
            evaluationCycle = em.find(EvaluationCycle.class, dto.getId());
            if (evaluationCycle == null) {
                evaluationCycle = new EvaluationCycle();
            }
        } else {
            evaluationCycle = new EvaluationCycle();
        }

        evaluationCycle.setCreatedAt(dto.getCreatedAt());
        evaluationCycle.setStartDate(dto.getStartDate());
        evaluationCycle.setEndDate(dto.getEndDate());
        evaluationCycle.setClosureDate(dto.getClosureDate());
        evaluationCycle.setStatus(dto.getStatus());

        // Handle relationships
        if (dto.getCreatedById() != null) {
            User createdBy = em.find(User.class, dto.getCreatedById());
            evaluationCycle.setCreatedBy(createdBy);
        }

        return evaluationCycle;
    }
}
