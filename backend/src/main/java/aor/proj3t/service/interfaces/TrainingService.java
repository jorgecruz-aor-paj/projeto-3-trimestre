package aor.proj3t.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.filters.TrainingFilter;

public interface TrainingService {

    /**
     * Create a new training.
     * 
     * @param newTrainingDTO the training to be added to the database
     * @return The created training
     */
    TrainingDTO createTraining(NewTrainingDTO newTrainingDTO);

    /**
     * Update an existing training.
     *
     * @param trainingDTO the training to be added to the database
     * @return The updated training
     */
    TrainingDTO updateTraining(TrainingDTO trainingDTO);

    /**
     * Deactivate a training.
     * 
     * @param trainingId The ID of the training to deactivate
     * @return true if deactivation was successful, false otherwise
     */
    Boolean deactivateTraining(Long trainingId);

    List<TrainingDTO> getAllTrainings();

    TrainingDTO getTrainingById(Long trainingId);

    List<TrainingAreaDTO> getAllTrainingAreas();

    TrainingAreaDTO addTrainingArea(TrainingAreaDTO trainingAreaDTO);

    List<TrainingDTO> getUnparticipatedTrainingsByUserId(Long requesterId, Long userId);

    List<TrainingDTO> getTrainingsByFilters(Long requesterId, TrainingFilter filters);

}
