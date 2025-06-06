package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TrainingConverter extends AbstractConverter<Training, TrainingDTO, Long> {

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
        dto.setName(training.getTitle());
        dto.setDuration(training.getDuration());
        dto.setDescription(training.getDescription());
        dto.setLink(training.getLink());
        dto.setLanguage(training.getLanguage());
        dto.setActive(training.isActive());
        dto.setCreatedAt(training.getCreatedAt());
        dto.setEditedAt(training.getEditedAt());

        if (training.getTrainingArea() != null) {
            dto.setTrainingAreaId(training.getTrainingArea().getId());
        }
    }

    @Override
    protected void mapDTOToEntity(TrainingDTO dto, Training training) {
        training.setTitle(dto.getName());
        training.setDuration(dto.getDuration());
        training.setDescription(dto.getDescription());
        training.setLink(dto.getLink());
        training.setLanguage(dto.getLanguage());
        training.setActive(dto.isActive());
        training.setCreatedAt(dto.getCreatedAt());
        training.setEditedAt(dto.getEditedAt());

        // Handle relationships using findSafely
        TrainingArea trainingArea = findSafely(TrainingArea.class, dto.getTrainingAreaId());
        training.setTrainingArea(trainingArea);
    }
}
