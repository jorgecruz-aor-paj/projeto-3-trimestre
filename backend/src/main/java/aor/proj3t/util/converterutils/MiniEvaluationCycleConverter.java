package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniEvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
/**
 * Converter class for transforming {@link EvaluationCycle} entities into compact DTOs.
 * <p>
 * Provides utilities to convert individual and lists of {@link EvaluationCycle} objects
 * into {@link MiniEvaluationCycleDTO}, which include only essential fields such as
 * ID, start date, and closure date. Intended for lightweight summaries and overviews.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class MiniEvaluationCycleConverter {

    /**
     * Converts a single {@link EvaluationCycle} to a {@link MiniEvaluationCycleDTO}.
     *
     * @param evaluationCycle the source evaluation cycle entity
     * @return a compact DTO with limited fields, or null if input is null
     */
    public MiniEvaluationCycleDTO toMiniEvaluationCycleDTO(EvaluationCycle evaluationCycle) {
        if (evaluationCycle == null) {
            return null;
        }
        return new MiniEvaluationCycleDTO(
                evaluationCycle.getId(),
                evaluationCycle.getStartDate(),
                evaluationCycle.getClosureDate()
        );
    }

    /**
     * Converts a list of {@link EvaluationCycle} entities to a list of {@link MiniEvaluationCycleDTO}.
     *
     * @param evaluationCycles the list of evaluation cycle entities
     * @return a list of compact DTOs; empty list if input is null
     */
    public List<MiniEvaluationCycleDTO> toMiniEvaluationCycleDTOList(List<EvaluationCycle> evaluationCycles) {
        if (evaluationCycles == null) {
            return new ArrayList<>();
        }
        List<MiniEvaluationCycleDTO> miniEvaluationCycleDTOList = new ArrayList<>(evaluationCycles.size());
        for (EvaluationCycle evaluationCycle : evaluationCycles) {
            miniEvaluationCycleDTOList.add(toMiniEvaluationCycleDTO(evaluationCycle));
        }
        return miniEvaluationCycleDTOList;
    }
}

