package aor.proj3t.service.impl;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import aor.proj3t.dto.NewTrainingParticipationDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
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
    private static final Logger logger = LogManager.getLogger(TrainingParticipationServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    @Override
    public List<TrainingParticipationDTO> findTrainingParticipationsByUser(Long requesterId, Long userId) {
            User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
            //access to Admins, the user himself, or the user's manager
        if (!requester.getId().equals(user.getId()) &&
                (requester.getRole() != Role.ADMIN &&
                        (user.getManager() == null || !user.getManager().getId().equals(requesterId)))) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with id " + requesterId + " requested invalid access to user with id " + userId + " training participation");
        }

        List<TrainingParticipation> trainingParticipationList = daoFactory.forTrainingParticipation()
                    .findByParticipantId(user.getId());
            return converterFactory.forTrainingParticipation().toDTOList(trainingParticipationList);
    }

    @Override
    public void registerTrainingParticipation(Long requesterId, NewTrainingParticipationDTO newTrainingParticipationDto) {
            User participant = getEntity(daoFactory.forUser().findById(newTrainingParticipationDto.getParticipantId()),
                    newTrainingParticipationDto.getParticipantId(), "User");
            User manager = getEntity(daoFactory.forUser().findById(newTrainingParticipationDto.getManagerId()),
                    newTrainingParticipationDto.getManagerId(), "User");
            //access by admin, or the user's manager
            if (manager.getRole() != Role.ADMIN && !Objects.equals(manager.getId(), participant.getManager().getId())) {
                throw new ForbiddenException(
                        "Permission denied",
                        "User with id " + manager.getId() + " adding participation to unmanaged user with id " + participant.getId());
            }
            Training training = getEntity(daoFactory.forTraining().findById(newTrainingParticipationDto.getTrainingId()),
                    newTrainingParticipationDto.getTrainingId(), "Training");
            TrainingParticipation trainingParticipation = new TrainingParticipation(
                    participant,
                    manager,
                    training,
                    newTrainingParticipationDto.getParticipatedAt());
            daoFactory.forTrainingParticipation().create(trainingParticipation);
    }

    @Override
    public Map<Integer,Long> getTrainingParticipationTimeByYearByUser(Long requesterId, Long userId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId),requesterId, "User");
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        if (!requester.getId().equals(user.getId()) &&
                (requester.getRole() != Role.ADMIN &&
                        (user.getManager() == null || !user.getManager().getId().equals(requesterId)))) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with id " + requesterId + " requested invalid access to user with id " + userId + " participation times by year");
        }
            return daoFactory.forTrainingParticipation().trainingTimeByYearByParticipantId(user.getId());

    }
}
