package aor.proj3t.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.entities.User;
import aor.proj3t.enums.TrainingLanguage;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.TrainingService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TrainingServiceImpl extends BaseService implements TrainingService {
    private static final Logger logger = LogManager.getLogger(TrainingServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    @Inject
    ServiceFactory serviceFactory;

    @Override
    public TrainingDTO createTraining(NewTrainingDTO newTrainingDto) {
        TrainingArea trainingArea = getEntity(
                daoFactory.forTrainingArea().findById(newTrainingDto.getTrainingAreaId()),
                newTrainingDto.getTrainingAreaId(), "Training area");
        Training training = new Training(
                newTrainingDto.getTitle(),
                newTrainingDto.getDuration(),
                newTrainingDto.getDescription(),
                newTrainingDto.getLink(),
                newTrainingDto.getLanguage(),
                trainingArea);
        daoFactory.forTraining().create(training);
        return converterFactory.forTraining().toDTO(training);
    }

    @Override
    public TrainingDTO updateTraining(TrainingDTO trainingDTO) {
        TrainingArea trainingArea = getEntity(
                daoFactory.forTrainingArea().findById(trainingDTO.getTrainingArea().getId()),
                trainingDTO.getTrainingArea().getId(), "Training area");
        Training training = getEntity(daoFactory.forTraining().findById(trainingDTO.getId()), trainingDTO.getId(),
                "Training");
        training.setTitle(trainingDTO.getTitle());
        training.setDuration(trainingDTO.getDuration());
        training.setDescription(trainingDTO.getDescription());
        training.setLink(trainingDTO.getLink());
        training.setLanguage(trainingDTO.getLanguage());
        training.setTrainingArea(trainingArea);
        daoFactory.forTraining().update(training);
        return converterFactory.forTraining().toDTO(training);
    }

    @Override
    public Boolean deactivateTraining(Long trainingId) {
        try {
            Training training = getEntity(daoFactory.forTraining().findById(trainingId), trainingId, "Training");
            String trainingTitle = training.getTitle();
            training.setActive(false);
            daoFactory.forTraining().update(training);

            // Send notification to all managers about training deactivation
            try {
                // For simplicity, we'll get all users and filter by role, or just send to a
                // known admin
                List<User> allUsers = daoFactory.forUser().findAll();
                for (User user : allUsers) {
                    if (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER) {
                        Notification notification = new Notification();
                        notification.setSender(user); // Self-notification for now
                        notification.setRecipient(user);
                        notification.setType(NotificationType.DELETE);
                        notification.setContent("Training '" + trainingTitle + "' has been deactivated.");
                        serviceFactory.forNotification().createNotification(notification);
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to send notifications for deactivated training {}", trainingId, e);
            }

            return true;
        } catch (NoSuchElementException e) {
            logger.error("Training with id {} not found while deactivating training", trainingId);
            return false;
        } catch (Exception e) {
            logger.error("Error deactivating training", e);
            return false;
        }
    }

    @Override
    public List<TrainingAreaDTO> getAllTrainingAreas() {
        return converterFactory.forTrainingArea().toDTOList(daoFactory.forTrainingArea().findAll());
    }

    @Override
    public TrainingAreaDTO addTrainingArea(TrainingAreaDTO trainingAreaDTO) {
        daoFactory.forTrainingArea()
                .findByName(trainingAreaDTO.getName().toLowerCase())
                .ifPresent(existing -> {
                    throw new ConflictException(
                            "Training area already exists",
                            "Adding new training area with duplicate name: " + trainingAreaDTO.getName());
                });
        TrainingArea trainingArea = new TrainingArea(trainingAreaDTO.getName());
        daoFactory.forTrainingArea().create(trainingArea);
        return converterFactory.forTrainingArea().toDTO(trainingArea);
    }

    @Override
    public List<TrainingDTO> getTrainingsByFilters(Long requesterId, TrainingFilter filters) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        if (requester.getRole() != Role.ADMIN && !filters.getActiveOnly()) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with ID " + requesterId + " requested deactivated trainings");
        }
        filters.hydrate();
        return converterFactory.forTraining().toDTOList(daoFactory.forTraining().getTrainingsByFilters(filters));

    }

    @Override
    public List<TrainingDTO> getUnparticipatedTrainingsByUserId(Long requesterId, Long userId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");

        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isManager = user.getManager() != null && user.getManager().getId().equals(requesterId);

        if (!isAdmin && !isManager) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with ID " + requesterId + " is not authorized to access unparticipated trainings for user ID "
                            + userId);
        }

        return converterFactory.forTraining().toDTOList(
                daoFactory.forTraining().findTrainingsNotParticipatedByUser(userId));
    }

}
