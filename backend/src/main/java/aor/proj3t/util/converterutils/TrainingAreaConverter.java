package aor.proj3t.util.converterutils;

import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.entities.TrainingArea;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Converter for TrainingArea entities and DTOs.
 * 
 * This class handles the bidirectional conversion between {@link TrainingArea}
 * entities and
 * {@link TrainingAreaDTO} data transfer objects. Training areas represent the
 * categories
 * or departments that training courses belong to.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
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
