package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.Workplace;
import aor.proj3t.service.interfaces.WorkplaceService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WorkplaceServiceImpl implements WorkplaceService {

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    @Override
    public List<WorkplaceDTO> getAllWorkplaces() {
        List<Workplace> list = daoFactory.forWorkplace().findAll();
        return converterFactory.forWorkplace().toDTOList(list);
    }
}
