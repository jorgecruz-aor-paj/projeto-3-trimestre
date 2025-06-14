package aor.proj3t.dto.paginated;

import aor.proj3t.config.AppConstants;

/**
 * Class representing sorting information for paginated queries.
 */
public class SortInfo {
    private String field;
    private boolean ascending;

    public SortInfo() {
        this.field = AppConstants.Pagination.DEFAULT_SORT_FIELD;
        this.ascending = false; // Default to descending as in constants
    }

    public SortInfo(String field, boolean ascending) {
        this.field = field != null && !field.isEmpty() ? field : AppConstants.Pagination.DEFAULT_SORT_FIELD;
        this.ascending = ascending;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Parse a sort string in the format "field,direction"
     * where direction is either "asc" or "desc"
     */
    public static SortInfo fromString(String sortString) {
        if (sortString == null || sortString.isEmpty()) {
            return new SortInfo();
        }

        String[] parts = sortString.split(",");
        String field = parts[0];
        boolean ascending = parts.length > 1 ? "asc".equalsIgnoreCase(parts[1]) : false;

        return new SortInfo(field, ascending);
    }

    /**
     * Convert to string format "field,direction"
     */
    @Override
    public String toString() {
        return field + "," + (ascending ? "asc" : "desc");
    }
}
