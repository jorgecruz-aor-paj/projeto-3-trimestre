package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.TrainingParticipation;

import java.util.List;
import java.util.Map;

public interface TrainingParticipationDAO extends BasicDAO<TrainingParticipation, Long> {

    List<TrainingParticipation> findByTrainingId(Long trainingId);

    List<TrainingParticipation> findByParticipantId(Long participantId);

    List<TrainingParticipation> findByManagerId(Long managerId);

    Map<Integer,Long> trainingTimeByYearByParticipantId(Long participationId);

}
