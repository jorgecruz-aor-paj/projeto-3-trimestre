package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniEvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MiniEvaluationCycleConverter {

    public MiniEvaluationCycleDTO toMiniEvaluationCycleDTO(EvaluationCycle evaluationCycle) {
        if (evaluationCycle == null) {
            return null;
        }
        return new MiniEvaluationCycleDTO(evaluationCycle.getId(), evaluationCycle.getStartDate(),evaluationCycle.getClosureDate());
    }

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
