package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.entities.TrainingArea;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TrainingAreaConverter extends AbstractConverter<TrainingArea, TrainingAreaDTO, Long> {

    @Override
    protected Class<TrainingArea> getEntityClass() {
        return TrainingArea.class;
    }

    @Override
    protected TrainingArea createEntityInstance() {
        return new TrainingArea();
    }

    @Override
    protected TrainingAreaDTO createDTOInstance() {
        return new TrainingAreaDTO();
    }

    @Override
    protected Long getIdFromDTO(TrainingAreaDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(TrainingArea trainingArea, TrainingAreaDTO dto) {
        dto.setId(trainingArea.getId());
        dto.setName(trainingArea.getName());
    }

    @Override
    protected void mapDTOToEntity(TrainingAreaDTO dto, TrainingArea trainingArea) {
        trainingArea.setName(dto.getName());
    }
}
