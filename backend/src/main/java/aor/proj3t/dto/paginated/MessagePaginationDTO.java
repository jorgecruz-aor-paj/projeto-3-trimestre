package aor.proj3t.dto.paginated;

import java.util.List;

import aor.proj3t.dto.MessageDTO;

/**
 * Data Transfer Object for paginated message search results.
 * <p>
 * This DTO combines a list of messages with pagination metadata like
 * total count, page size, current page, and total pages.
 * </p>
 */
public class MessagePaginationDTO extends PaginatedResponseDTO<MessageDTO> {

    // Default constructor required for JAX-RS
    public MessagePaginationDTO() {
    }

    /**
     * Creates a pagination DTO with all required data.
     * 
     * @param messages    The list of messages for the current page
     * @param totalCount  The total number of messages matching the search criteria
     * @param pageSize    The number of messages per page
     * @param currentPage The current page number (0-based)
     */
    public MessagePaginationDTO(List<MessageDTO> messages, long totalCount, int pageSize, int currentPage) {
        super(messages, totalCount, pageSize, currentPage);
    }
}
