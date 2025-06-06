package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.Workplace;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkplaceConverter extends AbstractConverter<Workplace, WorkplaceDTO, Long> {

    @Override
    protected Class<Workplace> getEntityClass() {
        return Workplace.class;
    }

    @Override
    protected Workplace createEntityInstance() {
        return new Workplace();
    }

    @Override
    protected WorkplaceDTO createDTOInstance() {
        return new WorkplaceDTO();
    }

    @Override
    protected Long getIdFromDTO(WorkplaceDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(Workplace workplace, WorkplaceDTO dto) {
        dto.setId(workplace.getId());
        dto.setName(workplace.getName());
    }

    @Override
    protected void mapDTOToEntity(WorkplaceDTO dto, Workplace workplace) {
        workplace.setName(dto.getName());
    }
}
