package aor.proj3t.service.interfaces;

import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.filters.TrainingFilter;
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

    /**
     * Exports all evaluations to a CSV format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @return a String containing the CSV data.
     */
    String exportEvaluationsToCSV(Long requesterId);

    /**
     * Exports filtered evaluations to a CSV format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @param filters     the filters to apply to the evaluation list.
     * @return a String containing the CSV data.
     */
    String exportFilteredEvaluationsToCSV(Long requesterId, EvaluationFilter filters);

    /**
     * Exports all trainings to a CSV format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @return a String containing the CSV data.
     */
    String exportTrainingsToCSV(Long requesterId);

    /**
     * Exports filtered trainings to a CSV format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @param filters     the filters to apply to the training list.
     * @return a String containing the CSV data.
     */
    String exportFilteredTrainingsToCSV(Long requesterId, TrainingFilter filters);
}
