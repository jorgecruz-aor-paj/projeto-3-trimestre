package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.*;
import aor.proj3t.service.interfaces.TrainingService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LogManager.getLogger(TrainingServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

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
            daoFactory.forTraining().create(training);
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
            daoFactory.forTraining().update(training);
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
            daoFactory.forTraining().update(training);
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
            List<TrainingParticipation> trainingParticipationList = daoFactory.forTrainingParticipation().findByParticipantId(user.getId());
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
    public List<TrainingDTO> getTrainingsUnparticipatedByUser(Long userId) {
        try {
            User user = getUser(userId);
            List<Training> trainingList = daoFactory.forTraining().findTrainingsNotParticipatedByUser(user.getId());
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
    public List<TrainingDTO> getAllTrainings() {
        try {
            List<Training> trainingList = daoFactory.forTraining().findAll();
            return converterFactory.forTraining().toDTOList(trainingList);
        } catch (Exception e) {
            logger.error("Error finding all trainings", e);
            return new ArrayList<>();
        }
    }

    @Override
    public TrainingDTO getTrainingById(Long trainingId) {
        return converterFactory.forTraining().toDTO(getTraining(trainingId));
    }

    @Override
    public List<TrainingAreaDTO> getAllTrainingAreas() {
        return converterFactory.forTrainingArea().toDTOList(daoFactory.forTrainingArea().findAll());
    }

    @Override
    public Boolean addTrainingArea(TrainingAreaDTO trainingAreaDTO) {
        try {
            TrainingArea trainingArea = new TrainingArea(trainingAreaDTO.getName());
            daoFactory.forTrainingArea().create(trainingArea);
            return true;
        } catch (Exception e) {
            logger.error("Error adding training area", e);
            return false;
        }
    }

    @Override
    public Boolean registerTrainingParticipation(TrainingParticipationDTO trainingParticipationDTO) {
        try {
            User participant = getUser(trainingParticipationDTO.getParticipantId());
            User manager = getUser(trainingParticipationDTO.getManagerId());
            Training training = getTraining(trainingParticipationDTO.getTrainingId());
            TrainingParticipation trainingParticipation = new TrainingParticipation(
                    participant,
                    manager,
                    training,
                    trainingParticipationDTO.getParticipatedAt());
            daoFactory.forTrainingParticipation().create(trainingParticipation);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Invalid id for participant {}, manager {} or training {}, while registering participation",
                    trainingParticipationDTO.getParticipantId(), trainingParticipationDTO.getManagerId(), trainingParticipationDTO.getTrainingId(), e);
            return false;
        } catch (Exception e) {
            logger.error("Error registering participation", e);
            return false;
        }
    }

    public Map<Integer, Integer> getTrainingParticipationTimeByYearByUser(Long userId) {
        try {
            User user = getUser(userId);
            return daoFactory.forTrainingParticipation().trainingTimeByYearByParticipantId(user.getId());
        } catch (NoSuchElementException e) {
            logger.error("No user found with id {}", userId);
            return null;
        } catch (Exception e) {
            logger.error("Error finding training participation time by year for user with id {}", e);
            return null;
        }
    }

    private TrainingArea getTrainingArea(Long trainingAreaId) {
        return daoFactory.forTrainingArea().findById(trainingAreaId)
                .orElseThrow(() -> new NoSuchElementException("TrainingArea not found with ID: " + trainingAreaId));
    }

    private Training getTraining(Long trainingId) {
        return daoFactory.forTraining().findById(trainingId)
                .orElseThrow(() -> new NoSuchElementException("Training not found with ID: " + trainingId));
    }

    private User getUser(Long userId) {
        return daoFactory.forUser().findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
    }
}
