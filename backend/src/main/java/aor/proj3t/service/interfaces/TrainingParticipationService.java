package aor.proj3t.service.interfaces;

import aor.proj3t.dto.NewTrainingParticipationDTO;
import aor.proj3t.dto.TrainingParticipationDTO;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Service interface for training participation management operations.
 * This interface defines the contract for managing user participation in
 * training programs
 * including registration, completion tracking, certification, and participation
 * analytics.
 * 
 * <p>
 * Key training participation services include:
 * <ul>
 * <li><b>Participation Registration:</b> Enrolling users in training
 * programs</li>
 * <li><b>Completion Tracking:</b> Monitoring and updating training completion
 * status</li>
 * <li><b>Participation History:</b> Retrieving user training participation
 * records</li>
 * <li><b>Analytics & Reporting:</b> Training participation statistics and time
 * analysis</li>
 * <li><b>Certificate Management:</b> Handling training certificates and
 * completions</li>
 * <li><b>Notification Integration:</b> Automatic notifications for
 * participation events</li>
 * </ul>
 * 
 * <p>
 * This service ensures proper tracking of user training progress, maintains
 * comprehensive
 * training records, and provides valuable participation analytics for training
 * program evaluation.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
public interface TrainingParticipationService {

    /**
     * Registers a user's participation in a training program.
     * Creates a new training participation record and handles notification
     * workflows.
     * 
     * @param requesterId                 the ID of the user making the registration
     *                                    request
     * @param newTrainingParticipationDTO the participation data including training
     *                                    and user details
     * @throws ForbiddenException if the requester lacks permission to register
     *                            participation
     * @throws ConflictException  if the user is already registered for the training
     */
    void registerTrainingParticipation(Long requesterId, NewTrainingParticipationDTO newTrainingParticipationDTO);

    /**
     * Retrieves all training participation records for a specific user.
     * Returns comprehensive participation history including completion status and
     * certificates.
     * 
     * @param requesterId the ID of the user making the request (for authorization)
     * @param userId      the ID of the user whose participation records to retrieve
     * @return list of TrainingParticipationDTO objects representing the user's
     *         training history
     * @throws ForbiddenException if the requester lacks permission to view user's
     *                            training data
     */
    List<TrainingParticipationDTO> findTrainingParticipationsByUser(Long requesterId, Long userId);

    /**
     * Retrieves training participation time statistics by year for a specific user.
     * Provides annual breakdown of training time investment for performance
     * analysis.
     * 
     * @param requesterId the ID of the user making the request (for authorization)
     * @param userId      the ID of the user whose training time statistics to
     *                    retrieve
     * @return LinkedHashMap with year as key and total training time (in hours) as
     *         value
     * @throws ForbiddenException if the requester lacks permission to view user's
     *                            training statistics
     */
    LinkedHashMap<Integer, Long> getTrainingParticipationTimeByYearByUser(Long requesterId, Long userId);

}
