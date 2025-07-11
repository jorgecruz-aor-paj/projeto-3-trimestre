package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.dto.WorkplaceDTO;

/**
 * Service interface for workplace management operations.
 * This interface defines the contract for workplace data management and
 * operations
 * within the organizational structure of the application.
 * 
 * <p>
 * Key workplace services include:
 * <ul>
 * <li><b>Workplace Retrieval:</b> Fetching all available workplace
 * locations</li>
 * <li><b>Data Conversion:</b> Converting workplace entities to DTOs for client
 * consumption</li>
 * <li><b>Organizational Structure:</b> Supporting user-workplace
 * associations</li>
 * </ul>
 * 
 * <p>
 * This service provides the foundation for workplace management and serves as
 * a basis for organizational structure within the user management system.
 * Future enhancements may include workplace creation, updates, and detailed
 * management.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
public interface WorkplaceService {

    /**
     * Retrieves all available workplaces in the system.
     * Fetches all workplace entities and converts them to DTOs for client
     * consumption.
     * 
     * @return a list of WorkplaceDTO objects representing all available workplaces
     */
    List<WorkplaceDTO> getAllWorkplaces();
}
