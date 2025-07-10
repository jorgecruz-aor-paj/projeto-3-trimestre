package aor.proj3t.util.converterutils;

import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Converter for TrainingParticipation entities and DTOs.
 * 
 * This class handles the bidirectional conversion between
 * {@link TrainingParticipation} entities and
 * {@link TrainingParticipationDTO} data transfer objects. Training
 * participations track when users
 * have completed training courses, including timestamps and approval by
 * managers.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class TrainingParticipationConverter
        extends AbstractConverter<TrainingParticipation, TrainingParticipationDTO, Long> {

    @Inject
    TrainingConverter trainingConverter;

    @Override
    protected Class<TrainingParticipation> getEntityClass() {
        return TrainingParticipation.class;
    }

    @Override
    protected TrainingParticipation createEntityInstance() {
        return new TrainingParticipation();
    }

    @Override
    protected TrainingParticipationDTO createDTOInstance() {
        return new TrainingParticipationDTO();
    }

    @Override
    protected Long getIdFromDTO(TrainingParticipationDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(TrainingParticipation participation, TrainingParticipationDTO dto) {
        dto.setId(participation.getId());
        dto.setCreatedAt(participation.getCreatedAt());
        dto.setParticipatedAt(participation.getParticipatedAt());

        if (participation.getParticipant() != null) {
            dto.setParticipantId(participation.getParticipant().getId());
        }

        if (participation.getManager() != null) {
            dto.setManagerId(participation.getManager().getId());
        }

        if (participation.getTraining() != null) {
            dto.setTraining(trainingConverter.toDTO(participation.getTraining()));
        }
    }

    @Override
    protected void mapDTOToEntity(TrainingParticipationDTO dto, TrainingParticipation participation) {
        participation.setCreatedAt(dto.getCreatedAt());
        participation.setParticipatedAt(dto.getParticipatedAt());

        // Handle relationships using findSafely
        User participant = findSafely(User.class, dto.getParticipantId());
        participation.setParticipant(participant);

        User manager = findSafely(User.class, dto.getManagerId());
        participation.setManager(manager);

        Training training = findSafely(Training.class, dto.getTraining() != null ? dto.getTraining().getId() : null);
        participation.setTraining(training);

    }
}
