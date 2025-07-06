package aor.proj3t.dto.paginated;

import aor.proj3t.config.AppConstants;

import java.util.List;

/**
 * Generic Data Transfer Object for paginated responses.
 * 
 * @param <T> The type of entity being paginated.
 */
public class PaginatedResponseDTO<T> {

    private List<T> entities;
    private long totalCount;
    private int pageSize;
    private int currentPage;
    private int totalPages;
    private SortInfo sortInfo;

    // Default constructor
    public PaginatedResponseDTO() {
    }

    /**
     * Creates a paginated response DTO with all required data.
     * 
     * @param entities    The list of entities for the current page
     * @param totalCount  The total number of entities matching the criteria
     * @param pageSize    The number of entities per page
     * @param currentPage The current page number (0-based)
     */
    public PaginatedResponseDTO(List<T> entities, long totalCount, int pageSize, int currentPage, SortInfo sortInfo) {
        this.entities = entities;
        this.totalCount = totalCount;
        this.pageSize = validatePageSize(pageSize);
        this.currentPage = validatePageNumber(currentPage);
        this.totalPages = calculateTotalPages(totalCount, pageSize);
        this.sortInfo = sortInfo != null ? sortInfo : new SortInfo();
    }

    // Getters and setters
    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        this.totalPages = calculateTotalPages(totalCount, pageSize);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.totalPages = calculateTotalPages(totalCount, pageSize);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public SortInfo getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(SortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }

    /**
     * Calculates the total number of pages based on item count and page size.
     */
    private int calculateTotalPages(long totalCount, int pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    private int validatePageSize(int pageSize) {
        if (pageSize <= 0) {
            return AppConstants.Pagination.DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, AppConstants.Pagination.MAX_PAGE_SIZE);
    }

    private int validatePageNumber(int pageNumber) {
        return Math.max(0, pageNumber);
    }
}