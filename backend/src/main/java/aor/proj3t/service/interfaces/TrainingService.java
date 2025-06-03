package aor.proj3t.service.interfaces;

import java.util.List;
import java.util.Optional;

import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingParticipation;

public interface TrainingService {

    /**
     * Create a new training.
     * 
     * @param name          The name of the training
     * @param durationHours The duration of the training in hours
     * @param description   The description of the training
     * @param contentLink   The link to the training content
     * @param language      The language of the training
     * @param area          The area of the training
     * @param createdById   The ID of the user who created the training
     * @return The created training
     */
    Training createTraining(String name, Integer durationHours, String description,
            String contentLink, String language, String area, Long createdById);

    /**
     * Update an existing training.
     * 
     * @param trainingId    The ID of the training to update
     * @param name          The name of the training
     * @param durationHours The duration of the training in hours
     * @param description   The description of the training
     * @param contentLink   The link to the training content
     * @param language      The language of the training
     * @param area          The area of the training
     * @return The updated training
     */
    Training updateTraining(Long trainingId, String name, Integer durationHours, String description,
            String contentLink, String language, String area);

    /**
     * Find a training by ID.
     * 
     * @param trainingId The ID of the training
     * @return An Optional containing the training if found, or empty if not found
     */
    Optional<Training> findById(Long trainingId);

    /**
     * Deactivate a training.
     * 
     * @param trainingId The ID of the training to deactivate
     * @return true if deactivation was successful, false otherwise
     */
    boolean deactivateTraining(Long trainingId);

    /**
     * Enroll a user in a training.
     * 
     * @param userId     The ID of the user
     * @param trainingId The ID of the training
     * @return The created UserTraining entity
     */
    TrainingParticipation enrollUser(Long userId, Long trainingId);

    /**
     * Find trainings for a specific user.
     * 
     * @param userId The ID of the user
     * @return A list of trainings for the specified user
     */
    List<TrainingParticipation> findTrainingsByUser(Long userId);

    // Convert to DTO methods
    //
}
