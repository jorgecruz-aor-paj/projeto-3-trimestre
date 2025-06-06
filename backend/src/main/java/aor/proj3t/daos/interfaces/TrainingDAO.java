package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.Training;
import aor.proj3t.filters.TrainingFilter;

public interface TrainingDAO extends BasicDAO<Training, Long> {

    Boolean setAsInactive(Long trainingId);

    List<Training> getTrainingsByFilters(TrainingFilter filters);

    List<Training> findTrainingsNotParticipatedByUser(Long userId);

}
