package aor.proj3t.service.interfaces;

import java.time.Instant;
import java.util.List;

import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.enums.EvaluationCycleStatus;

public interface EvaluationCycleService {


    Boolean startCycle(Long adminId, Instant startTime, Instant endTime);

    EvaluationCycleDTO getActiveCycle();

    List<EvaluationCycleDTO> getAllCycles();

    Boolean updateCycleStatus(Long cycleId, EvaluationCycleStatus status);
}
