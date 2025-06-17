package aor.proj3t.service.interfaces;

import java.time.Instant;
import java.util.List;

import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.filters.DateRange;

public interface EvaluationCycleService {


    void startCycle(Long adminId, DateRange dateRange);

    EvaluationCycleDTO getActiveCycle(Long requesterId);

    List<EvaluationCycleDTO> getAllCycles(Long requesterId, boolean active);

    Boolean updateCycleStatus(Long cycleId, EvaluationCycleStatus status);
}
