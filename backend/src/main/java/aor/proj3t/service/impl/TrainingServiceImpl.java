package aor.proj3t.service.impl;

import aor.proj3t.daos.impl.TrainingAreaDAOimpl;
import aor.proj3t.daos.impl.TrainingDAOimpl;
import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.daos.interfaces.TrainingParticipationDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.*;
import aor.proj3t.service.interfaces.TrainingService;
import aor.proj3t.util.converterUtils.ConverterFactory;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LogManager.getLogger(TrainingServiceImpl.class);


    @Inject
    TrainingAreaDAOimpl trainingAreaDAO;

    @Inject
    TrainingDAOimpl trainingDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    TrainingParticipationDAO trainingParticipationDAO;

    @Inject
    ConverterFactory converterFactory;


    @Override
    public Boolean createTraining(TrainingDTO trainingDTO) {
        try {
            TrainingArea trainingArea = getTrainingArea(trainingDTO.getTrainingAreaId());
            Training training = new Training(
                    trainingDTO.getTitle(),
                    trainingDTO.getDuration(),
                    trainingDTO.getDescription(),
                    trainingDTO.getLink(),
                    trainingDTO.getLanguage(),
                    trainingArea
            );
            trainingDAO.create(training);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Training area not found with id: " + trainingDTO.getTrainingAreaId());
            return false;
        } catch (Exception e) {
            logger.error("Error creating training", e);
            return false;
        }
    }

    @Override
    public Boolean updateTraining(TrainingDTO trainingDTO) {
        try {
            TrainingArea trainingArea = getTrainingArea(trainingDTO.getTrainingAreaId());
            Training training = getTraining(trainingDTO.getId());
            training.setTitle(trainingDTO.getTitle());
            training.setDuration(trainingDTO.getDuration());
            training.setDescription(trainingDTO.getDescription());
            training.setLink(trainingDTO.getLink());
            training.setLanguage(trainingDTO.getLanguage());
            training.setTrainingArea(trainingArea);
            trainingDAO.update(training);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Training area with id {} or training with id {} not found while updating training", trainingDTO.getTrainingAreaId(), trainingDTO.getId());
            return false;
        } catch (Exception e) {
            logger.error("Error updating training", e);
            return false;
        }
    }

    @Override
    public Boolean deactivateTraining(Long trainingId) {
        try {
            Training training = getTraining(trainingId);
            training.setActive(false);
            trainingDAO.update(training);
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
    public List<TrainingParticipationDTO> findTrainingParticipationsByUser(Long userId) {
        try {
            User user = getUser(userId);
            List<TrainingParticipation> trainingParticipationList = trainingParticipationDAO.findByParticipantId(user.getId());
            return converterFactory.forTrainingParticipation().toDTOList(trainingParticipationList);
        } catch (NoSuchElementException e) {
            logger.error("No user found with id {}", userId);
            return null;
        } catch (Exception e) {
            logger.error("Error finding training participations", e);
            return null;
        }
    }

    @Override
    public List<TrainingDTO> findTrainingsUnparticipatedByUser(Long userId) {
        try {
            User user = getUser(userId);
            List<Training> trainingList = trainingDAO.findTrainingsNotParticipatedByUser(user.getId());
            return converterFactory.forTraining().toDTOList(trainingList);
        } catch (NoSuchElementException e) {
            logger.error("No user found with id {}", userId);
            return null;
        } catch (Exception e) {
            logger.error("Error finding trainigs not participated by user", e);
            return null;
        }
    }

    @Override
    public List<TrainingDTO> findAllTrainings() {
        try{
            List<Training> trainingList = trainingDAO.findAll();
            return converterFactory.forTraining().toDTOList(trainingList);
        }catch (Exception e) {
            logger.error("Error finding all trainings", e);
            return new ArrayList<>();
        }
    }


    private TrainingArea getTrainingArea(Long trainingAreaId) {
        return trainingAreaDAO.findById(trainingAreaId)
                .orElseThrow(() -> new NoSuchElementException("TrainingArea not found with ID: " + trainingAreaId));
    }

    private Training getTraining(Long trainingId) {
        return trainingDAO.findById(trainingId)
                .orElseThrow(() -> new NoSuchElementException("Training not found with ID: " + trainingId));
    }

    private User getUser(Long userId) {
        return userDAO.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
    }
}
