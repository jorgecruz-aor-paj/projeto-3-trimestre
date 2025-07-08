package aor.proj3t.service.interfaces;

import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.filters.TrainingFilter;

import java.util.List;

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

    List<TrainingAreaDTO> getAllTrainingAreas();

    TrainingAreaDTO addTrainingArea(TrainingAreaDTO trainingAreaDTO);

    List<TrainingDTO> getUnparticipatedTrainingsByUserId(Long requesterId, Long userId);

    List<TrainingDTO> getTrainingsByFilters(Long requesterId, TrainingFilter filters);

}
