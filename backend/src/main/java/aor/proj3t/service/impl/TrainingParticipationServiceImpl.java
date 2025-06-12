package aor.proj3t.service.impl;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import aor.proj3t.service.interfaces.TrainingParticipationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TrainingParticipationServiceImpl extends BaseService implements TrainingParticipationService {
    private static final Logger logger = LogManager.getLogger(TrainingParticipationService.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    @Override
    public List<TrainingParticipationDTO> findTrainingParticipationsByUser(Long userId) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
            List<TrainingParticipation> trainingParticipationList = daoFactory.forTrainingParticipation()
                    .findByParticipantId(user.getId());
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
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
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
    public Boolean registerTrainingParticipation(TrainingParticipationDTO trainingParticipationDTO) {
        try {
            User participant = getEntity(daoFactory.forUser().findById(trainingParticipationDTO.getParticipantId()),
                    trainingParticipationDTO.getParticipantId(), "User");
            User manager = getEntity(daoFactory.forUser().findById(trainingParticipationDTO.getManagerId()),
                    trainingParticipationDTO.getManagerId(), "User");
            if (manager.getId() != participant.getManager().getId()) {
                logger.warn("User manager and trainingParticipation manager id's are a mismatch");
                return false;
            }
            Training training = getEntity(daoFactory.forTraining().findById(trainingParticipationDTO.getTrainingId()),
                    trainingParticipationDTO.getTrainingId(), "Training");
            TrainingParticipation trainingParticipation = new TrainingParticipation(
                    participant,
                    manager,
                    training,
                    trainingParticipationDTO.getParticipatedAt());
            daoFactory.forTrainingParticipation().create(trainingParticipation);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Invalid id for participant {}, manager {} or training {}, while registering participation",
                    trainingParticipationDTO.getParticipantId(), trainingParticipationDTO.getManagerId(),
                    trainingParticipationDTO.getTrainingId(), e);
            return false;
        } catch (Exception e) {
            logger.error("Error registering participation", e);
            return false;
        }
    }

    public Map<Integer, Integer> getTrainingParticipationTimeByYearByUser(Long userId) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
            return daoFactory.forTrainingParticipation().trainingTimeByYearByParticipantId(user.getId());
        } catch (NoSuchElementException e) {
            logger.error("No user found with id {}", userId);
            return null;
        } catch (Exception e) {
            logger.error("Error finding training participation time by year for user with id {}", e);
            return null;
        }
    }
}
