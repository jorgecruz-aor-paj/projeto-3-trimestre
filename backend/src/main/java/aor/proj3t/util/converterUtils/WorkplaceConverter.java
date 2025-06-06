package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.Workplace;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class WorkplaceConverter implements Converter<Workplace, WorkplaceDTO> {

    @Inject
    private EntityManager em;

    @Override
    public WorkplaceDTO toDTO(Workplace workplace) {
        if (workplace == null) {
            return null;
        }

        return new WorkplaceDTO(
                workplace.getId(),
                workplace.getName());
    }

    @Override
    public Workplace toEntity(WorkplaceDTO dto) {
        if (dto == null) {
            return null;
        }

        Workplace workplace;
        if (dto.getId() != null && dto.getId() != 0) {
            workplace = em.find(Workplace.class, dto.getId());
            if (workplace == null) {
                workplace = new Workplace();
            }
        } else {
            workplace = new Workplace();
        }

        workplace.setName(dto.getName());

        return workplace;
    }
}
