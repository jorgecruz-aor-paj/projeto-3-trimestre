package aor.proj3t.service.interfaces;

import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.filters.DateRange;

import java.util.List;

public interface EvaluationCycleService {


    void startCycle(Long adminId, DateRange dateRange);

    EvaluationCycleDTO getActiveCycle(Long requesterId);

    List<EvaluationCycleDTO> getAllCycles(Long requesterId, boolean active);

    void sendCycleClosureEmail(EvaluationCycle cycle);
}
