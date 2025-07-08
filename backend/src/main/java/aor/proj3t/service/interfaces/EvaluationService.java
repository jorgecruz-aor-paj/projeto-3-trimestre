package aor.proj3t.service.interfaces;

import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.entities.User;
import aor.proj3t.filters.EvaluationFilter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface EvaluationService {

    /**
     * Update an evaluation.
     * 
     * @param requesterId The ID of requester
     * @param evaluationDTO The evaluationDto to update the DataBase
     */
    EvaluationDTO updateEvaluation(Long requesterId, EvaluationDTO evaluationDTO);

    void closeEvaluationsBulk (Long requesterId, EvaluationCloseDTO evaluationCloseDto);

    List<EvaluationDTO> getEvaluationsByFilter(Long requesterId, EvaluationFilter filter);

    Map<String, Long> getEvaluationsCompletedStats(Long requesterId);


   }
