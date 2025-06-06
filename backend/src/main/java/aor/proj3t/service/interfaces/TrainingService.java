package aor.proj3t.service.interfaces;

import java.util.List;
import java.util.Optional;

import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.entities.TrainingParticipation;

public interface TrainingService {

    /**
     * Create a new training.
     * 
     * @param trainingDTO the training to be added to the database
     * @return The created training
     */
    Boolean createTraining(TrainingDTO trainingDTO);

    /**
     * Update an existing training.
     *
     * @param trainingDTO the training to be added to the database
     * @return The updated training
     */
    Boolean updateTraining(TrainingDTO trainingDTO);

    /**
     * Deactivate a training.
     * 
     * @param trainingId The ID of the training to deactivate
     * @return true if deactivation was successful, false otherwise
     */
    Boolean deactivateTraining(Long trainingId);

    /**
     * Find trainings for a specific user.
     * 
     * @param userId The ID of the user
     * @return A list of trainings for the specified user
     */
    List<TrainingParticipationDTO> findTrainingParticipationsByUser(Long userId);

    List<TrainingDTO> findTrainingsUnparticipatedByUser(Long userId);

    List<TrainingDTO> findAllTrainings();

    TrainingDTO getTrainingById(Long trainingId);

    List<TrainingAreaDTO> getAllTrainingAreas();

    Boolean addTrainingArea(TrainingAreaDTO trainingAreaDTO);

    Boolean registerTrainingParticipation(TrainingParticipationDTO trainingParticipationDTO);


}
