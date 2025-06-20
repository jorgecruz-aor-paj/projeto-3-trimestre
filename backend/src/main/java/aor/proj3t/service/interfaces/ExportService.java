package aor.proj3t.service.interfaces;

import aor.proj3t.filters.UserFilter;

public interface ExportService {

    /**
     * Exports all users to a CSV format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @return a String containing the CSV data.
     */
    String exportUsersToCSV(Long requesterId);

    /**
     * Exports filtered users to a CSV format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @param filters     the filters to apply to the user list.
     * @return a String containing the CSV data.
     */
    String exportFilteredUsersToCSV(Long requesterId, UserFilter filters);
}
