package aor.proj3t.util.converterutils;

import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Converter for Training entities and DTOs.
 * 
 * This class handles the bidirectional conversion between {@link Training}
 * entities and
 * {@link TrainingDTO} data transfer objects. It manages training course
 * information including
 * descriptions, duration, language, active status, and associated training
 * areas.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class TrainingConverter extends AbstractConverter<Training, TrainingDTO, Long> {

    @Inject
    TrainingAreaConverter trainingAreaConverter;

    @Override
    protected Class<Training> getEntityClass() {
        return Training.class;
    }

    @Override
    protected Training createEntityInstance() {
        return new Training();
    }

    @Override
    protected TrainingDTO createDTOInstance() {
        return new TrainingDTO();
    }

    @Override
    protected Long getIdFromDTO(TrainingDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(Training training, TrainingDTO dto) {
        dto.setId(training.getId());
        dto.setTitle(training.getTitle());
        dto.setDuration(training.getDuration());
        dto.setDescription(training.getDescription());
        dto.setLink(training.getLink());
        dto.setLanguage(training.getLanguage() != null ? training.getLanguage() : null);
        dto.setActive(training.isActive());
        dto.setCreatedAt(training.getCreatedAt());
        dto.setEditedAt(training.getEditedAt());

        if (training.getTrainingArea() != null) {
            dto.setTrainingArea(trainingAreaConverter.toDTO(training.getTrainingArea()));
        }
    }

    @Override
    protected void mapDTOToEntity(TrainingDTO dto, Training training) {
        training.setTitle(dto.getTitle());
        training.setDuration(dto.getDuration());
        training.setDescription(dto.getDescription());
        training.setLink(dto.getLink());
        training.setLanguage(dto.getLanguage());
        training.setActive(dto.isActive());
        training.setCreatedAt(dto.getCreatedAt());
        training.setEditedAt(dto.getEditedAt());

        // Handle relationships using findSafely
        TrainingArea trainingArea = findSafely(TrainingArea.class, dto.getTrainingArea() != null ? dto.getTrainingArea().getId() : null);
        training.setTrainingArea(trainingArea);
    }
}
