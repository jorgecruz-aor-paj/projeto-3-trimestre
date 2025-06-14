package aor.proj3t.dto.paginated;

import java.util.List;

import aor.proj3t.dto.TrainingDTO;

/**
 * Data Transfer Object for paginated training search results.
 * <p>
 * This DTO combines a list of trainings with pagination metadata like
 * total count, page size, current page, and total pages.
 * </p>
 */
public class TrainingPaginationDTO extends PaginatedResponseDTO<TrainingDTO> {

    // Default constructor required for JAX-RS
    public TrainingPaginationDTO() {
    }

    /**
     * Creates a pagination DTO with all required data.
     * 
     * @param trainings   The list of trainings for the current page
     * @param totalCount  The total number of trainings matching the search criteria
     * @param pageSize    The number of trainings per page
     * @param currentPage The current page number (0-based)
     */
    public TrainingPaginationDTO(List<TrainingDTO> trainings, long totalCount, int pageSize, int currentPage) {
        super(trainings, totalCount, pageSize, currentPage);
    }
}
