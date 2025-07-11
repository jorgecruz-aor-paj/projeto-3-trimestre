package aor.proj3t.service.impl;

import java.util.List;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.Workplace;
import aor.proj3t.service.interfaces.WorkplaceService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of the WorkplaceService interface.
 * This service handles workplace-related operations including retrieval
 * and management of workplace information.
 * 
 * <p>
 * Currently provides functionality for:
 * <ul>
 * <li>Retrieving all available workplaces</li>
 * <li>Converting workplace entities to DTOs for client consumption</li>
 * </ul>
 * 
 * <p>
 * This service serves as a foundation for workplace management
 * and can be extended to support additional workplace operations.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class WorkplaceServiceImpl implements WorkplaceService {

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkplaceDTO> getAllWorkplaces() {
        List<Workplace> list = daoFactory.forWorkplace().findAll();
        return converterFactory.forWorkplace().toDTOList(list);
    }
}
