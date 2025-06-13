package aor.proj3t.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import aor.proj3t.dto.NewTrainingDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.filters.TrainingFilter;
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

    @Override
    public void createTraining(NewTrainingDTO newTrainingDto) {
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
    }

    @Override
    public Boolean updateTraining(TrainingDTO trainingDTO) {
        try {
            TrainingArea trainingArea = getEntity(
                    daoFactory.forTrainingArea().findById(trainingDTO.getTrainingAreaId()),
                    trainingDTO.getTrainingAreaId(), "Training area");
            Training training = getEntity(daoFactory.forTraining().findById(trainingDTO.getId()), trainingDTO.getId(), "Training");
            training.setTitle(trainingDTO.getTitle());
            training.setDuration(trainingDTO.getDuration());
            training.setDescription(trainingDTO.getDescription());
            training.setLink(trainingDTO.getLink());
            training.setLanguage(trainingDTO.getLanguage());
            training.setTrainingArea(trainingArea);
            daoFactory.forTraining().update(training);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Training area with id {} or training with id {} not found while updating training",
                    trainingDTO.getTrainingAreaId(), trainingDTO.getId());
            return false;
        } catch (Exception e) {
            logger.error("Error updating training", e);
            return false;
        }
    }

    @Override
    public Boolean deactivateTraining(Long trainingId) {
        try {
            Training training = getEntity(daoFactory.forTraining().findById(trainingId), trainingId, "Training");
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
        Training training = getEntity(daoFactory.forTraining().findById(trainingId), trainingId, "Training");
        return converterFactory.forTraining().toDTO(training);
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
    public List<TrainingDTO> getTrainingsByFilters(TrainingFilter filters) {
        try {
            return converterFactory.forTraining().toDTOList(daoFactory.forTraining().getTrainingsByFilters(filters));
        } catch (Exception e) {
            logger.error("Error finding trainings by filter", e);
            return null;
        }
    }

}
