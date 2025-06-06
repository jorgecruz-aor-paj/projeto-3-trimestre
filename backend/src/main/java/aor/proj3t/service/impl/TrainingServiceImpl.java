package aor.proj3t.service.impl;

import aor.proj3t.daos.impl.TrainingAreaDAOimpl;
import aor.proj3t.daos.impl.TrainingDAOimpl;
import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.daos.interfaces.TrainingParticipationDAO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.Workplace;
import aor.proj3t.service.interfaces.TrainingService;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.NoSuchElementException;

public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LogManager.getLogger(TrainingServiceImpl.class);


    @Inject
    TrainingAreaDAOimpl trainingAreaDAO;

    @Inject
    TrainingDAOimpl trainingDAO;

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
        return false;
    }

    @Override
    public TrainingParticipation enrollUser(Long userId, Long trainingId) {
        return null;
    }

    @Override
    public List<TrainingParticipation> findTrainingsByUser(Long userId) {
        return List.of();
    }


    private TrainingArea getTrainingArea(Long trainingAreaId) {
        return trainingAreaDAO.findById(trainingAreaId)
                .orElseThrow(() -> new NoSuchElementException("TrainingArea not found with ID: " + trainingAreaId));
    }

    private Training getTraining(Long trainingId) {
        return trainingDAO.findById(trainingId)
                .orElseThrow(() -> new NoSuchElementException("Training not found with ID: " + trainingId));
    }
}
