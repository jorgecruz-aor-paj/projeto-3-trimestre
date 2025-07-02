package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniEvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MiniEvaluationCycleConverter {

    public MiniEvaluationCycleDTO toMiniEvaluationCycleDTO(EvaluationCycle evaluationCycle) {
        return new MiniEvaluationCycleDTO(evaluationCycle.getId(), evaluationCycle.getStartDate(),evaluationCycle.getClosureDate());
    }

    public List<MiniEvaluationCycleDTO> toMiniEvaluationCycleDTOList(List<EvaluationCycle> evaluationCycles) {
        List<MiniEvaluationCycleDTO> miniEvaluationCycleDTOList = new ArrayList<>(evaluationCycles.size());
        for (EvaluationCycle evaluationCycle : evaluationCycles) {
            miniEvaluationCycleDTOList.add(toMiniEvaluationCycleDTO(evaluationCycle));
        }
        return miniEvaluationCycleDTOList;
    }
}
