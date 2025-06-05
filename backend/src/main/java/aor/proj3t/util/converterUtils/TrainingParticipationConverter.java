package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class TrainingParticipationConverter implements Converter<TrainingParticipation, TrainingParticipationDTO> {

    @Inject
    private EntityManager em;

    @Override
    public TrainingParticipationDTO toDTO(TrainingParticipation participation) {
        if (participation == null) {
            return null;
        }

        return new TrainingParticipationDTO(
                participation.getId(),
                participation.getParticipant() != null ? participation.getParticipant().getId() : null,
                participation.getManager() != null ? participation.getManager().getId() : null,
                participation.getTraining() != null ? participation.getTraining().getId() : null,
                participation.getCreatedAt(),
                participation.getParticipatedAt());
    }

    @Override
    public TrainingParticipation toEntity(TrainingParticipationDTO dto) {
        if (dto == null) {
            return null;
        }

        TrainingParticipation participation;
        if (dto.getId() != null && dto.getId() != 0) {
            participation = em.find(TrainingParticipation.class, dto.getId());
            if (participation == null) {
                participation = new TrainingParticipation();
            }
        } else {
            participation = new TrainingParticipation();
        }

        participation.setCreatedAt(dto.getCreatedAt());
        participation.setParticipatedAt(dto.getParticipatedAt());

        // Handle relationships
        if (dto.getParticipantId() != null) {
            User participant = em.find(User.class, dto.getParticipantId());
            participation.setParticipant(participant);
        }

        if (dto.getManagerId() != null) {
            User manager = em.find(User.class, dto.getManagerId());
            participation.setManager(manager);
        }

        if (dto.getTrainingId() != null) {
            Training training = em.find(Training.class, dto.getTrainingId());
            participation.setTraining(training);
        }

        return participation;
    }
}
