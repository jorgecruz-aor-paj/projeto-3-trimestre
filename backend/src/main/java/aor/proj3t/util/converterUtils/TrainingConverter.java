package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class TrainingConverter implements Converter<Training, TrainingDTO> {

    @Inject
    private EntityManager em;

    @Override
    public TrainingDTO toDTO(Training training) {
        if (training == null) {
            return null;
        }

        return new TrainingDTO(
                training.getId(),
                training.getTitle(),
                training.getDuration(),
                training.getDescription(),
                training.getLink(),
                training.getLanguage(),
                training.isActive(),
                training.getCreatedAt(),
                training.getEditedAt(),
                training.getTrainingArea() != null ? training.getTrainingArea().getId() : null);
    }

    @Override
    public Training toEntity(TrainingDTO dto) {
        if (dto == null) {
            return null;
        }

        Training training;
        if (dto.getId() != null && dto.getId() != 0) {
            training = em.find(Training.class, dto.getId());
            if (training == null) {
                training = new Training();
            }
        } else {
            training = new Training();
        }

        training.setTitle(dto.getName());
        training.setDuration(dto.getDuration());
        training.setDescription(dto.getDescription());
        training.setLink(dto.getLink());
        training.setLanguage(dto.getLanguage());
        training.setActive(dto.isActive());
        training.setCreatedAt(dto.getCreatedAt());
        training.setEditedAt(dto.getEditedAt());

        // Handle relationships
        if (dto.getTrainingAreaId() != null) {
            TrainingArea trainingArea = em.find(TrainingArea.class, dto.getTrainingAreaId());
            training.setTrainingArea(trainingArea);
        }

        return training;
    }
}
