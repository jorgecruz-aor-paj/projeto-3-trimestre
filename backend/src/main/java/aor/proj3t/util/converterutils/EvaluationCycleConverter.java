package aor.proj3t.util.converterutils;

import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Hibernate;

import java.util.List;

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

    @Inject
    EvaluationConverter evaluationConverter;

    @Override
    public Class<EvaluationCycle> getEntityClass() {
        return EvaluationCycle.class;
    }

    @Override
    public EvaluationCycle createEntityInstance() {
        return new EvaluationCycle();
    }

    @Override
    public EvaluationCycleDTO createDTOInstance() {
        return new EvaluationCycleDTO();
    }

    @Override
    public Long getIdFromDTO(EvaluationCycleDTO dto) {
        return dto.getId();
    }

    @Override
    public void mapEntityToDTO(EvaluationCycle evaluationCycle, EvaluationCycleDTO dto) {
        dto.setId(evaluationCycle.getId());
        dto.setCreatedAt(evaluationCycle.getCreatedAt());
        dto.setStartDate(evaluationCycle.getStartDate());
        dto.setEndDate(evaluationCycle.getEndDate());
        dto.setClosureDate(evaluationCycle.getClosureDate());
        dto.setStatus(evaluationCycle.getStatus());

        if (evaluationCycle.getCreatedBy() != null) {
            dto.setCreatedById(evaluationCycle.getCreatedBy().getId());
        }

        if (Hibernate.isInitialized(evaluationCycle.getEvaluations()) && evaluationCycle.getEvaluations() != null) {
            List<EvaluationDTO> evaluationDTOs = evaluationConverter
                    .toDTOList(evaluationCycle.getEvaluations());
            dto.setEvaluations(evaluationDTOs);
        }

    }


    @Override
    public void mapDTOToEntity(EvaluationCycleDTO dto, EvaluationCycle evaluationCycle) {
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
