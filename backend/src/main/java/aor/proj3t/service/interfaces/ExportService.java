package aor.proj3t.service.interfaces;

import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.filters.UserFilter;

/**
 * Service interface for data export operations.
 * This interface defines the contract for comprehensive data export
 * functionality
 * supporting multiple formats (CSV, PDF) and various data types including
 * users, evaluations, and trainings.
 * 
 * <p>
 * Key export services include:
 * <ul>
 * <li><b>User Data Export:</b> Comprehensive user information export with
 * filtering capabilities</li>
 * <li><b>Evaluation Export:</b> Detailed evaluation data export with
 * performance metrics</li>
 * <li><b>Training Export:</b> Training program and participation data
 * export</li>
 * <li><b>Multiple Formats:</b> Support for CSV and PDF export formats</li>
 * <li><b>Advanced Filtering:</b> Customizable data selection and filtering
 * options</li>
 * <li><b>Professional Formatting:</b> Well-structured documents with proper
 * styling</li>
 * </ul>
 * 
 * <p>
 * This service provides essential reporting and data analysis capabilities for
 * administrators
 * and managers, enabling data extraction for further processing, archival, or
 * compliance purposes.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
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

    /**
     * Exports a single evaluation to a PDF format file.
     * <p>
     * This method exports a specific evaluation with its detailed information to
     * PDF.
     * It includes all relevant data about the evaluation, evaluator, and evaluatee.
     * </p>
     *
     * @param requesterId  the ID of the user requesting the export.
     * @param evaluationId the ID of the evaluation to export.
     * @return a byte array containing the PDF data.
     */
    byte[] exportEvaluationToPDF(Long requesterId, Long evaluationId);

    // XLSX Export Methods

    /**
     * Exports all users to an XLSX format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @return a byte array containing the XLSX data.
     */
    byte[] exportUsersToXLSX(Long requesterId);

    /**
     * Exports filtered users to an XLSX format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @param filters     the filters to apply to the user list.
     * @return a byte array containing the XLSX data.
     */
    byte[] exportFilteredUsersToXLSX(Long requesterId, UserFilter filters);

    /**
     * Exports all evaluations to an XLSX format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @return a byte array containing the XLSX data.
     */
    byte[] exportEvaluationsToXLSX(Long requesterId);

    /**
     * Exports filtered evaluations to an XLSX format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @param filters     the filters to apply to the evaluation list.
     * @return a byte array containing the XLSX data.
     */
    byte[] exportFilteredEvaluationsToXLSX(Long requesterId, EvaluationFilter filters);

    /**
     * Exports all trainings to an XLSX format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @return a byte array containing the XLSX data.
     */
    byte[] exportTrainingsToXLSX(Long requesterId);

    /**
     * Exports filtered trainings to an XLSX format file.
     *
     * @param requesterId the ID of the user requesting the export.
     * @param filters     the filters to apply to the training list.
     * @return a byte array containing the XLSX data.
     */
    byte[] exportFilteredTrainingsToXLSX(Long requesterId, TrainingFilter filters);
}
