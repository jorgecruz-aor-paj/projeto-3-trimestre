package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.filters.TrainingFilter;

/**
 * Service interface for training management operations.
 * This interface defines the contract for comprehensive training program
 * management
 * including creation, updates, approval workflows, and training area
 * categorization.
 * 
 * <p>
 * Key training services include:
 * <ul>
 * <li><b>Training Lifecycle:</b> Creation, updates, and deactivation of
 * training programs</li>
 * <li><b>Training Areas:</b> Management and categorization of training subject
 * areas</li>
 * <li><b>Advanced Filtering:</b> Complex training search based on multiple
 * criteria</li>
 * <li><b>User-specific Views:</b> Personalized training recommendations and
 * unparticipated trainings</li>
 * <li><b>Approval Workflows:</b> Training approval and publication
 * processes</li>
 * <li><b>Integration Support:</b> Notification system integration for training
 * events</li>
 * </ul>
 * 
 * <p>
 * This service ensures proper training program management with authorization
 * checks,
 * maintains training categorization, and provides comprehensive training
 * discovery capabilities.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
public interface TrainingService {

    /**
     * Creates a new training program with the provided information.
     * Validates the training area existence and creates the training entity.
     * 
     * @param newTrainingDTO the training data for creation including title,
     *                       description, and area
     * @return a TrainingDTO representing the created training program
     * @throws NotFoundException if the specified training area doesn't exist
     */
    TrainingDTO createTraining(NewTrainingDTO newTrainingDTO);

    /**
     * Updates an existing training program with new information.
     * Validates the training and training area existence before updating.
     * 
     * @param trainingDTO the training data containing updated information
     * @return a TrainingDTO representing the updated training program
     * @throws NotFoundException if the training or training area doesn't exist
     */
    TrainingDTO updateTraining(TrainingDTO trainingDTO);

    /**
     * Deactivates a training program, making it unavailable for new participation.
     * Maintains existing participation records while preventing new enrollments.
     * 
     * @param trainingId the unique identifier of the training to deactivate
     * @return true if deactivation was successful, false otherwise
     * @throws NotFoundException if the training doesn't exist
     */
    Boolean deactivateTraining(Long trainingId);

    /**
     * Retrieves all available training areas for categorization purposes.
     * Provides the complete list of training subject categories.
     * 
     * @return a list of TrainingAreaDTO objects representing all training areas
     */
    List<TrainingAreaDTO> getAllTrainingAreas();

    /**
     * Adds a new training area category to the system.
     * Creates a new subject area for organizing training programs.
     * 
     * @param trainingAreaDTO the training area data containing name and description
     * @return a TrainingAreaDTO representing the created training area
     * @throws ConflictException if a training area with the same name already
     *                           exists
     */
    TrainingAreaDTO addTrainingArea(TrainingAreaDTO trainingAreaDTO);

    /**
     * Retrieves training programs that a specific user has not yet participated in.
     * Provides personalized training recommendations based on participation
     * history.
     * 
     * @param requesterId the ID of the user making the request (for authorization)
     * @param userId      the ID of the user to find unparticipated trainings for
     * @return a list of TrainingDTO objects representing available training
     *         programs
     * @throws ForbiddenException if the requester lacks permission to view user's
     *                            training data
     */
    List<TrainingDTO> getUnparticipatedTrainingsByUserId(Long requesterId, Long userId);

    /**
     * Retrieves training programs based on specified filtering criteria.
     * Supports complex filtering by area, status, date ranges, and other training
     * attributes.
     * 
     * @param requesterId the ID of the user making the request (used for
     *                    authorization)
     * @param filters     the filter criteria for training search
     * @return a list of TrainingDTO objects matching the filter criteria
     */
    List<TrainingDTO> getTrainingsByFilters(Long requesterId, TrainingFilter filters);

}
