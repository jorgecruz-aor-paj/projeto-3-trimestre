package aor.proj3t.service.interfaces;

import aor.proj3t.dto.NewTrainingParticipationDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface TrainingParticipationService {

   void registerTrainingParticipation(Long requesterId, NewTrainingParticipationDTO newTrainingParticipationDTO);

    List<TrainingParticipationDTO> findTrainingParticipationsByUser(Long requesterId, Long userId);

    LinkedHashMap<Integer,Long> getTrainingParticipationTimeByYearByUser(Long requesterId, Long userId);

}
