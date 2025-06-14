package aor.proj3t.dto.paginated;

import java.util.List;

import aor.proj3t.dto.NotificationDTO;

/**
 * Data Transfer Object for paginated notification search results.
 * <p>
 * This DTO combines a list of notifications with pagination metadata like
 * total count, page size, current page, and total pages.
 * </p>
 */
public class NotificationPaginationDTO extends PaginatedResponseDTO<NotificationDTO> {

    // Default constructor required for JAX-RS
    public NotificationPaginationDTO() {
    }

    /**
     * Creates a pagination DTO with all required data.
     * 
     * @param notifications The list of notifications for the current page
     * @param totalCount    The total number of notifications matching the search
     *                      criteria
     * @param pageSize      The number of notifications per page
     * @param currentPage   The current page number (0-based)
     */
    public NotificationPaginationDTO(List<NotificationDTO> notifications, long totalCount, int pageSize,
            int currentPage, SortInfo sortInfo) {
        super(notifications, totalCount, pageSize, currentPage, sortInfo);
    }
}
