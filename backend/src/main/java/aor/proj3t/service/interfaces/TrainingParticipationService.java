package aor.proj3t.service.interfaces;

import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;

import java.util.List;
import java.util.Map;

public interface TrainingParticipationService {

    Boolean registerTrainingParticipation(TrainingParticipationDTO trainingParticipationDTO);

    Map<Integer, Integer> getTrainingParticipationTimeByYearByUser(Long userId);

    List<TrainingParticipationDTO> findTrainingParticipationsByUser(Long userId);

    List<TrainingDTO> getTrainingsUnparticipatedByUser(Long userId);

}
