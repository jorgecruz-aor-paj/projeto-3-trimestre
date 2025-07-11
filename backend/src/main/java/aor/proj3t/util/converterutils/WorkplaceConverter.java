package aor.proj3t.util.converterutils;

import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.Workplace;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Converter for Workplace entities and DTOs.
 * 
 * This class handles the bidirectional conversion between {@link Workplace}
 * entities and
 * {@link WorkplaceDTO} data transfer objects. It provides simple mapping for
 * workplace
 * data used throughout the system.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class WorkplaceConverter extends AbstractConverter<Workplace, WorkplaceDTO, Long> {

    @Override
    public Class<Workplace> getEntityClass() {
        return Workplace.class;
    }

    @Override
    public Workplace createEntityInstance() {
        return new Workplace();
    }

    @Override
    public WorkplaceDTO createDTOInstance() {
        return new WorkplaceDTO();
    }

    @Override
    public Long getIdFromDTO(WorkplaceDTO dto) {
        return dto.getId();
    }

    @Override
    public void mapEntityToDTO(Workplace workplace, WorkplaceDTO dto) {
        dto.setId(workplace.getId());
        dto.setName(workplace.getName());
    }

    @Override
    public void mapDTOToEntity(WorkplaceDTO dto, Workplace workplace) {
        workplace.setName(dto.getName());
    }
}
