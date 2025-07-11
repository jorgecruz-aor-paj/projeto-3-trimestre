package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.NewTrainingParticipationDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.TrainingParticipationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the TrainingParticipationService interface.
 * This service manages the participation of users in training programs,
 * including
 * registration, completion tracking, certification, and participation
 * analytics.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Training participation registration and management</li>
 * <li>Completion status tracking and updates</li>
 * <li>Certificate generation and distribution</li>
 * <li>Training participation analytics and reporting</li>
 * <li>Notification integration for participation events</li>
 * <li>Authorization controls for participation management</li>
 * </ul>
 * 
 * <p>
 * This service ensures proper tracking of user training progress,
 * maintains training records, and provides comprehensive participation
 * analytics for training program evaluation and user development tracking.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class TrainingParticipationServiceImpl extends BaseService implements TrainingParticipationService {
    private static final Logger logger = LogManager.getLogger(TrainingParticipationServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    @Inject
    ServiceFactory serviceFactory;

    @Override
    public List<TrainingParticipationDTO> findTrainingParticipationsByUser(Long requesterId, Long userId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        // access to Admins, the user himself, or the user's manager
        if (!requester.getId().equals(user.getId()) &&
                (requester.getRole() != Role.ADMIN &&
                        (user.getManager() == null
                                || !user.getManager().getId().equals(requesterId)))) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with id " + requesterId + " requested invalid access to user with id "
                            + userId + " training participation");
        }

        List<TrainingParticipation> trainingParticipationList = daoFactory.forTrainingParticipation()
                .findByParticipantId(user.getId());
        return converterFactory.forTrainingParticipation().toDTOList(trainingParticipationList);
    }

    @Override
    public void registerTrainingParticipation(Long requesterId,
            NewTrainingParticipationDTO newTrainingParticipationDto) {
        User participant = getEntity(
                daoFactory.forUser().findById(newTrainingParticipationDto.getParticipantId()),
                newTrainingParticipationDto.getParticipantId(), "User");
        User manager = getEntity(daoFactory.forUser().findById(newTrainingParticipationDto.getManagerId()),
                newTrainingParticipationDto.getManagerId(), "User");
        // access by admin, or the user's manager
        if (manager.getRole() != Role.ADMIN
                && !Objects.equals(manager.getId(), participant.getManager().getId())) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with id " + manager.getId()
                            + " adding participation to unmanaged user with id "
                            + participant.getId());
        }
        Training training = getEntity(
                daoFactory.forTraining().findById(newTrainingParticipationDto.getTrainingId()),
                newTrainingParticipationDto.getTrainingId(), "Training");
        TrainingParticipation trainingParticipation = new TrainingParticipation(
                participant,
                manager,
                training,
                newTrainingParticipationDto.getParticipatedAt());
        daoFactory.forTrainingParticipation().create(trainingParticipation);

        // Send notification to participant about the new training participation
        try {
            Notification notification = new Notification();
            notification.setSender(manager);
            notification.setRecipient(participant);
            notification.setType(NotificationType.EDIT);
            notification.setContent("You have been registered for training: " + training.getTitle());
            serviceFactory.forNotification().createNotification(notification);
        } catch (Exception e) {
            // Log error but don't fail the registration
            logger.error("Failed to send notification for training participation registration", e);
        }
    }

    @Override
    public LinkedHashMap<Integer, Long> getTrainingParticipationTimeByYearByUser(Long requesterId, Long userId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");

        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isSelf = requester.getId().equals(user.getId());
        boolean isManager = user.getManager() != null && user.getManager().getId().equals(requesterId);

        if (!(isAdmin || isSelf || isManager)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with id " + requesterId + " is not authorized to access user " + userId
                            + "'s participation times.");
        }

        return daoFactory.forTrainingParticipation().trainingTimeByYearByParticipantId(user.getId());
    }

}
