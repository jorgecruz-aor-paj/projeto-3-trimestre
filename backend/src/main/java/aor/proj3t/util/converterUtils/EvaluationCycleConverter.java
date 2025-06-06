package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Converter for EvaluationCycle entities and DTOs.
 * 
 * This class handles the bidirectional conversion between
 * {@link EvaluationCycle} entities and
 * {@link EvaluationCycleDTO} data transfer objects. Evaluation cycles represent
 * time periods
 * during which employee performance evaluations are conducted, including start
 * dates,
 * end dates, and closure dates.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class EvaluationCycleConverter extends AbstractConverter<EvaluationCycle, EvaluationCycleDTO, Long> {

    @Override
    protected Class<EvaluationCycle> getEntityClass() {
        return EvaluationCycle.class;
    }

    @Override
    protected EvaluationCycle createEntityInstance() {
        return new EvaluationCycle();
    }

    @Override
    protected EvaluationCycleDTO createDTOInstance() {
        return new EvaluationCycleDTO();
    }

    @Override
    protected Long getIdFromDTO(EvaluationCycleDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(EvaluationCycle evaluationCycle, EvaluationCycleDTO dto) {
        dto.setId(evaluationCycle.getId());
        dto.setCreatedAt(evaluationCycle.getCreatedAt());
        dto.setStartDate(evaluationCycle.getStartDate());
        dto.setEndDate(evaluationCycle.getEndDate());
        dto.setClosureDate(evaluationCycle.getClosureDate());
        dto.setStatus(evaluationCycle.getStatus());

        if (evaluationCycle.getCreatedBy() != null) {
            dto.setCreatedById(evaluationCycle.getCreatedBy().getId());
        }
    }

    @Override
    protected void mapDTOToEntity(EvaluationCycleDTO dto, EvaluationCycle evaluationCycle) {
        evaluationCycle.setCreatedAt(dto.getCreatedAt());
        evaluationCycle.setStartDate(dto.getStartDate());
        evaluationCycle.setEndDate(dto.getEndDate());
        evaluationCycle.setClosureDate(dto.getClosureDate());
        evaluationCycle.setStatus(dto.getStatus());

        // Handle relationships using findSafely
        User createdBy = findSafely(User.class, dto.getCreatedById());
        evaluationCycle.setCreatedBy(createdBy);
    }
}
