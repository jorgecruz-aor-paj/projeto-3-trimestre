package aor.proj3t.dto.paginated;

import java.util.List;

import aor.proj3t.dto.UserDTO;

/**
 * Data Transfer Object for paginated user search results.
 * <p>
 * This DTO combines a list of users with pagination metadata like
 * total count, page size, current page, and total pages.
 * </p>
 */
public class UserPaginationDTO extends PaginatedResponseDTO<UserDTO> {

    // Default constructor required for JAX-RS
    public UserPaginationDTO() {
    }

    /**
     * Creates a pagination DTO with all required data.
     * 
     * @param users       The list of users for the current page
     * @param totalCount  The total number of users matching the search criteria
     * @param pageSize    The number of users per page
     * @param currentPage The current page number (0-based)
     */
    public UserPaginationDTO(List<UserDTO> users, long totalCount, int pageSize, int currentPage) {
        super(users, totalCount, pageSize, currentPage);
    }
}