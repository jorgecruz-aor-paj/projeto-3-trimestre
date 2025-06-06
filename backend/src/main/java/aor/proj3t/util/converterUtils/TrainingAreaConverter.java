package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.entities.TrainingArea;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class TrainingAreaConverter implements Converter<TrainingArea, TrainingAreaDTO> {

    @Inject
    private EntityManager em;

    @Override
    public TrainingAreaDTO toDTO(TrainingArea trainingArea) {
        if (trainingArea == null) {
            return null;
        }

        return new TrainingAreaDTO(
                trainingArea.getId(),
                trainingArea.getName());
    }

    @Override
    public TrainingArea toEntity(TrainingAreaDTO dto) {
        if (dto == null) {
            return null;
        }

        TrainingArea trainingArea;
        if (dto.getId() != null && dto.getId() != 0) {
            trainingArea = em.find(TrainingArea.class, dto.getId());
            if (trainingArea == null) {
                trainingArea = new TrainingArea();
            }
        } else {
            trainingArea = new TrainingArea();
        }

        trainingArea.setName(dto.getName());

        return trainingArea;
    }
}
