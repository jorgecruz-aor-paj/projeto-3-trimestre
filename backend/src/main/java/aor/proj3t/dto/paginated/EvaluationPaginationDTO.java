package aor.proj3t.dto.paginated;

import java.util.List;

import aor.proj3t.dto.EvaluationDTO;

/**
 * Data Transfer Object for paginated evaluation search results.
 * <p>
 * This DTO combines a list of evaluations with pagination metadata like
 * total count, page size, current page, and total pages.
 * </p>
 */
public class EvaluationPaginationDTO extends PaginatedResponseDTO<EvaluationDTO> {

    // Default constructor required for JAX-RS
    public EvaluationPaginationDTO() {
    }

    /**
     * Creates a pagination DTO with all required data.
     * 
     * @param evaluations The list of evaluations for the current page
     * @param totalCount  The total number of evaluations matching the search
     *                    criteria
     * @param pageSize    The number of evaluations per page
     * @param currentPage The current page number (0-based)
     */
    public EvaluationPaginationDTO(List<EvaluationDTO> evaluations, long totalCount, int pageSize, int currentPage,
            SortInfo sortInfo) {
        super(evaluations, totalCount, pageSize, currentPage, sortInfo);
    }
}
