package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.TrainingParticipation;

import java.util.LinkedHashMap;
import java.util.List;

public interface TrainingParticipationDAO extends BasicDAO<TrainingParticipation, Long> {

    List<TrainingParticipation> findByParticipantId(Long participantId);

    LinkedHashMap<Integer,Long> trainingTimeByYearByParticipantId(Long participationId);

}
