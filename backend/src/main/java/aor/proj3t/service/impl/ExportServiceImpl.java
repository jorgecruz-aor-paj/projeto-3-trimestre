package aor.proj3t.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.EvaluationCsvDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.dto.TrainingCsvDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.UserCsvDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.exceptions.BadRequestException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.interfaces.ExportService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of the ExportService interface.
 * This comprehensive service provides data export functionality in multiple
 * formats
 * including CSV and PDF, supporting various data types such as users,
 * evaluations, and trainings.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>User data export with filtering capabilities</li>
 * <li>Evaluation data export with comprehensive details</li>
 * <li>Training data export and reporting</li>
 * <li>CSV format export using OpenCSV library</li>
 * <li>PDF format export using iText library</li>
 * <li>Customizable data filtering and selection</li>
 * <li>Professional document formatting and styling</li>
 * </ul>
 * 
 * <p>
 * This service provides essential reporting and data analysis capabilities,
 * enabling administrators and managers to extract and analyze system data
 * in standardized formats for further processing or archival purposes.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class ExportServiceImpl extends BaseService implements ExportService {

    private static final Logger logger = LogManager.getLogger(ExportServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all users from the database and exports them to CSV format.
     * Verifies that the requesting user exists before proceeding.
     * </p>
     */
    @Override
    public String exportUsersToCSV(Long requesterId) {
        logger.info("Processing request to export all users to CSV from user ID: {}", requesterId);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get all users
        List<User> users = daoFactory.forUser().findAll();
        logger.debug("Retrieved {} users for export", users.size());

        // Process and return CSV
        String result = processUsersToCsv(users);
        logger.info("Successfully completed CSV export of all users");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves filtered users from the database and exports them to CSV format.
     * Verifies that the requesting user exists before proceeding.
     * </p>
     */
    @Override
    public String exportFilteredUsersToCSV(Long requesterId, UserFilter filters) {
        logger.info("Processing request to export filtered users to CSV from user ID: {}", requesterId);
        logger.debug("Applied filters: {}", filters);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get filtered users from the DAO
        List<User> users = daoFactory.forUser().getUsersByFilters(filters);
        logger.debug("Retrieved {} users matching filter criteria", users.size());

        // Process and return CSV
        String result = processUsersToCsv(users);
        logger.info("Successfully completed CSV export of filtered users");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all evaluations from the database and exports them to CSV format.
     * Verifies that the requesting user exists before proceeding.
     * </p>
     */
    @Override
    public String exportEvaluationsToCSV(Long requesterId) {
        logger.info("Processing request to export all evaluations to CSV from user ID: {}", requesterId);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get all evaluations
        List<Evaluation> evaluations = daoFactory.forEvaluation().findAll();
        logger.debug("Retrieved {} evaluations for export", evaluations.size());

        // Process and return CSV
        String result = processEvaluationsToCsv(evaluations);
        logger.info("Successfully completed CSV export of all evaluations");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves filtered evaluations from the database and exports them to CSV
     * format.
     * Verifies that the requesting user exists before proceeding.
     * </p>
     */
    @Override
    public String exportFilteredEvaluationsToCSV(Long requesterId, EvaluationFilter filters) {
        logger.info("Processing request to export filtered evaluations to CSV from user ID: {}", requesterId);
        logger.debug("Applied filters: {}", filters);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get filtered evaluations from the DAO
        List<Evaluation> evaluations = daoFactory.forEvaluation().getEvaluationsByFilters(filters);
        logger.debug("Retrieved {} evaluations matching filter criteria", evaluations.size());

        // Process and return CSV
        String result = processEvaluationsToCsv(evaluations);
        logger.info("Successfully completed CSV export of filtered evaluations");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all trainings from the database and exports them to CSV format.
     * Verifies that the requesting user exists before proceeding.
     * </p>
     */
    @Override
    public String exportTrainingsToCSV(Long requesterId) {
        logger.info("Processing request to export all trainings to CSV from user ID: {}", requesterId);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get all trainings
        List<Training> trainings = daoFactory.forTraining().findAll();
        logger.debug("Retrieved {} trainings for export", trainings.size());

        // Process and return CSV
        String result = processTrainingsToCsv(trainings);
        logger.info("Successfully completed CSV export of all trainings");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves filtered trainings from the database and exports them to CSV
     * format.
     * Verifies that the requesting user exists before proceeding.
     * </p>
     */
    @Override
    public String exportFilteredTrainingsToCSV(Long requesterId, TrainingFilter filters) {
        logger.info("Processing request to export filtered trainings to CSV from user ID: {}", requesterId);
        logger.debug("Applied filters: {}", filters);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get filtered trainings from the DAO
        List<Training> trainings = daoFactory.forTraining().getTrainingsByFilters(filters);
        logger.debug("Retrieved {} trainings matching filter criteria", trainings.size());

        // Process and return CSV
        String result = processTrainingsToCsv(trainings);
        logger.info("Successfully completed CSV export of filtered trainings");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Exports a single evaluation to PDF format with detailed information.
     * Verifies that the requesting user exists before proceeding.
     * </p>
     */
    @Override
    public byte[] exportEvaluationToPDF(Long requesterId, Long evaluationId) {
        logger.info("Processing request to export evaluation ID: {} to PDF from user ID: {}", evaluationId,
                requesterId);

        // Verify requester exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get the evaluation entity
        Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationId), evaluationId,
                "Evaluation");
        logger.debug("Retrieved evaluation with ID: {} for export", evaluationId);

        // Get related entities for more complete PDF
        User evaluator = getEntity(daoFactory.forUser().findById(evaluation.getEvaluator().getId()),
                evaluation.getEvaluator().getId(), "User (Evaluator)");

        User evaluated = getEntity(daoFactory.forUser().findById(evaluation.getEvaluated().getId()),
                evaluation.getEvaluated().getId(), "User (Evaluated)");

        // Generate and return PDF
        byte[] result = generateEvaluationPdf(evaluation, evaluator, evaluated);
        logger.info("Successfully completed PDF export of evaluation with ID: {}", evaluationId);

        return result;
    }

    /**
     * Verifies that the requesting user exists in the system.
     * <p>
     * This method checks if a user with the given ID exists in the database.
     * If not, a NotFoundException is thrown.
     * </p>
     * 
     * @param requesterId ID of the user making the request
     * @throws NotFoundException if user does not exist
     */
    private void verifyRequester(Long requesterId) {
        logger.debug("Verifying existence of requester with ID: {}", requesterId);
        getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        logger.debug("Requester verification successful");
    }

    /**
     * Processes a list of users into a CSV string.
     * <p>
     * This method converts User entities to DTOs, then to CSV-specific DTOs,
     * and finally generates a CSV string representation.
     * </p>
     * 
     * @param users List of user entities to convert to CSV
     * @return CSV string representation of users
     */
    private String processUsersToCsv(List<User> users) {
        logger.debug("Processing {} users for CSV export", users.size());

        // Convert to DTOs
        List<UserDTO> userDTOs = converterFactory.forUser().toDTOList(users);
        logger.debug("Converted users to DTOs");

        // Convert to CSV DTOs
        List<UserCsvDTO> userCsvDTOs = userDTOs.stream()
                .map(UserCsvDTO::new)
                .collect(Collectors.toList());
        logger.debug("Converted user DTOs to CSV-specific DTOs");

        // Generate CSV
        String result = generateCsvFromBeans(userCsvDTOs);
        logger.debug("Generated CSV string with {} characters", result.length());

        return result;
    }

    /**
     * Processes a list of evaluations into a CSV string.
     * <p>
     * This method converts Evaluation entities to DTOs, then to CSV-specific DTOs,
     * and finally generates a CSV string representation.
     * </p>
     * 
     * @param evaluations List of evaluation entities to convert to CSV
     * @return CSV string representation of evaluations
     */
    private String processEvaluationsToCsv(List<Evaluation> evaluations) {
        logger.debug("Processing {} evaluations for CSV export", evaluations.size());

        // Convert to DTOs
        List<EvaluationDTO> evaluationDTOs = converterFactory.forEvaluation().toDTOList(evaluations);
        logger.debug("Converted evaluations to DTOs");

        // Convert to CSV DTOs
        List<EvaluationCsvDTO> evaluationCsvDTOs = evaluationDTOs.stream()
                .map(EvaluationCsvDTO::new)
                .collect(Collectors.toList());
        logger.debug("Converted evaluation DTOs to CSV-specific DTOs");

        // Generate CSV
        String result = generateCsvFromBeans(evaluationCsvDTOs);
        logger.debug("Generated CSV string with {} characters", result.length());

        return result;
    }

    /**
     * Processes a list of trainings into a CSV string.
     * <p>
     * This method converts Training entities to DTOs, then to CSV-specific DTOs,
     * and finally generates a CSV string representation.
     * </p>
     * 
     * @param trainings List of training entities to convert to CSV
     * @return CSV string representation of trainings
     */
    private String processTrainingsToCsv(List<Training> trainings) {
        logger.debug("Processing {} trainings for CSV export", trainings.size());

        // Convert to DTOs
        List<TrainingDTO> trainingDTOs = converterFactory.forTraining().toDTOList(trainings);
        logger.debug("Converted trainings to DTOs");

        // Convert to CSV DTOs
        List<TrainingCsvDTO> trainingCsvDTOs = trainingDTOs.stream()
                .map(TrainingCsvDTO::new)
                .collect(Collectors.toList());
        logger.debug("Converted training DTOs to CSV-specific DTOs");

        // Generate CSV
        String result = generateCsvFromBeans(trainingCsvDTOs);
        logger.debug("Generated CSV string with {} characters", result.length());

        return result;
    }

    /**
     * Generates a PDF document for a single evaluation with detailed information.
     * <p>
     * This method creates a PDF with comprehensive details about an evaluation,
     * including information about the evaluator, evaluated user, and the evaluation
     * itself.
     * </p>
     * 
     * @param evaluation The evaluation entity to export to PDF
     * @param evaluator  The user who performed the evaluation
     * @param evaluated  The user who was evaluated
     * @return byte array containing the PDF document
     */
    private byte[] generateEvaluationPdf(Evaluation evaluation, User evaluator, User evaluated) {
        logger.debug("Generating PDF for evaluation with ID: {}", evaluation.getId());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(baos);
                PdfDocument pdf = new PdfDocument(writer)) {

            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(36, 36, 36, 36); // 0.5 inch margins

            // Add title
            Paragraph title = new Paragraph("Evaluation Report");
            title.setFontSize(20);
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // Add space

            // Add basic information table
            document.add(createBasicInfoTable(evaluation));

            document.add(new Paragraph(" ")); // Add space

            // Add user information
            document.add(createUserInfoTable(evaluator, evaluated));

            document.add(new Paragraph(" ")); // Add space

            // Add evaluation details
            document.add(createEvaluationDetailsTable(evaluation));

            document.add(new Paragraph(" ")); // Add space

            // Add feedback section if it exists
            if (evaluation.getFeedback() != null && !evaluation.getFeedback().isEmpty()) {
                document.add(createFeedbackSection(evaluation.getFeedback()));
            }

            // Add footer with generation timestamp
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);

            Paragraph footer = new Paragraph("Generated on: " + timestamp);
            footer.setFontSize(8);
            footer.setTextAlignment(TextAlignment.RIGHT);
            document.add(footer);

            document.close();

            byte[] result = baos.toByteArray();
            logger.debug("Successfully generated PDF with {} bytes", result.length);
            return result;

        } catch (Exception e) {
            logger.error("Error generating PDF for evaluation", e);
            throw new BadRequestException(
                    "Failed to generate PDF",
                    "An unexpected error occurred while generating the evaluation PDF. Please try again or contact support.");
        }
    }

    /**
     * Creates a table with basic information about the evaluation.
     * <p>
     * This method generates a two-column table containing essential evaluation
     * details
     * including ID, status, scale, creation date, and last modification date.
     * The table includes a header row with gray background for better visual
     * presentation.
     * </p>
     * 
     * @param evaluation The evaluation entity containing the data to display
     * @return Table with formatted evaluation information
     */
    private Table createBasicInfoTable(Evaluation evaluation) {
        Table table = new Table(UnitValue.createPercentArray(new float[] { 50, 50 }));
        table.setWidth(UnitValue.createPercentValue(100));

        // Format dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdAt = evaluation.getCreatedAt() != null
                ? LocalDateTime.ofInstant(evaluation.getCreatedAt(), ZoneId.systemDefault()).format(formatter)
                : "N/A";
        String editedAt = evaluation.getEditedAt() != null
                ? LocalDateTime.ofInstant(evaluation.getEditedAt(), ZoneId.systemDefault()).format(formatter)
                : "N/A";

        // Add rows
        Cell headerCell = new Cell(1, 2);
        headerCell.add(new Paragraph("Evaluation Information"));
        headerCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        headerCell.setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(headerCell);

        addTableRow(table, "Evaluation ID:", String.valueOf(evaluation.getId()));
        addTableRow(table, "Status:", String.valueOf(evaluation.getStatus()));
        addTableRow(table, "Scale:", String.valueOf(evaluation.getScale()));
        addTableRow(table, "Created At:", createdAt);
        addTableRow(table, "Last Edited At:", editedAt);

        return table;
    }

    /**
     * Creates a table with information about the evaluator and evaluated users.
     * <p>
     * This method generates a comprehensive table displaying information about both
     * the evaluator and the evaluated user, including their IDs, full names,
     * emails,
     * and roles. The table is divided into two sections with appropriate headers
     * for clear organization.
     * </p>
     * 
     * @param evaluator The user who performed the evaluation
     * @param evaluated The user who was evaluated
     * @return Table with formatted user information for both evaluator and
     *         evaluated
     */
    private Table createUserInfoTable(User evaluator, User evaluated) {
        Table table = new Table(UnitValue.createPercentArray(new float[] { 50, 50 }));
        table.setWidth(UnitValue.createPercentValue(100));

        // Evaluator section
        Cell evaluatorHeader = new Cell(1, 2);
        evaluatorHeader.add(new Paragraph("Evaluator Information"));
        evaluatorHeader.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        evaluatorHeader.setTextAlignment(TextAlignment.CENTER);
        table.addCell(evaluatorHeader);

        addTableRow(table, "Evaluator ID:", String.valueOf(evaluator.getId()));
        addTableRow(table, "Full Name:", evaluator.getFirstName() + " " + evaluator.getLastName());
        addTableRow(table, "Email:", evaluator.getEmail());
        addTableRow(table, "Role:", String.valueOf(evaluator.getRole()));

        // Evaluated user section
        Cell evaluatedHeader = new Cell(1, 2);
        evaluatedHeader.add(new Paragraph("Evaluated User Information"));
        evaluatedHeader.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        evaluatedHeader.setTextAlignment(TextAlignment.CENTER);
        table.addCell(evaluatedHeader);

        addTableRow(table, "Evaluated ID:", String.valueOf(evaluated.getId()));
        addTableRow(table, "Full Name:", evaluated.getFirstName() + " " + evaluated.getLastName());
        addTableRow(table, "Email:", evaluated.getEmail());
        addTableRow(table, "Role:", String.valueOf(evaluated.getRole()));

        return table;
    }

    /**
     * Creates a table with detailed evaluation status information.
     * <p>
     * This method generates a single-column table that displays status-specific
     * information about the evaluation. The content varies based on the
     * evaluation's
     * current status (ONGOING, COMPLETED, or CLOSED) to provide contextual
     * information.
     * </p>
     * 
     * @param evaluation The evaluation entity containing status information
     * @return Table with formatted evaluation status details
     */
    private Table createEvaluationDetailsTable(Evaluation evaluation) {
        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));

        Cell headerCell = new Cell();
        headerCell.add(new Paragraph("Evaluation Status Information"));
        headerCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        headerCell.setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(headerCell);

        // Add status-specific information
        Cell statusCell = new Cell();
        Paragraph statusInfo = new Paragraph();

        if (evaluation.getStatus() == EvaluationStatus.ONGOING) {
            statusInfo.add("This evaluation is still ongoing and waiting for completion.");
        } else if (evaluation.getStatus() == EvaluationStatus.COMPLETED) {
            statusInfo.add("This evaluation has been completed.");
        } else if (evaluation.getStatus() == EvaluationStatus.CLOSED) {
            statusInfo.add("This evaluation cycle has been closed.");
        }

        statusCell.add(statusInfo);
        table.addCell(statusCell);

        return table;
    }

    /**
     * Creates a section for displaying evaluation feedback.
     * <p>
     * This method generates a single-column table specifically designed to display
     * evaluation feedback text. The table includes a header row with gray
     * background
     * and a content cell containing the feedback text.
     * </p>
     * 
     * @param feedback The feedback text to display in the table
     * @return Table with formatted feedback section
     */
    private Table createFeedbackSection(String feedback) {
        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));

        Cell headerCell = new Cell();
        headerCell.add(new Paragraph("Feedback"));
        headerCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        headerCell.setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(headerCell);

        Cell feedbackCell = new Cell();
        feedbackCell.add(new Paragraph(feedback));
        table.addCell(feedbackCell);

        return table;
    }

    /**
     * Utility method to add a row to a table with label and value.
     * <p>
     * This helper method creates a two-cell row in a table, with the first cell
     * containing a label and the second cell containing the corresponding value.
     * Null values are automatically converted to "N/A" for better presentation.
     * </p>
     * 
     * @param table The table to add the row to
     * @param label The label text for the first cell
     * @param value The value text for the second cell (null values become "N/A")
     */
    private void addTableRow(Table table, String label, String value) {
        Cell labelCell = new Cell();
        Paragraph labelParagraph = new Paragraph(label);
        labelCell.add(labelParagraph);

        Cell valueCell = new Cell();
        valueCell.add(new Paragraph(value != null ? value : "N/A"));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    /**
     * Generates a CSV string from a list of beans using OpenCSV's bean-to-CSV
     * capabilities.
     * <p>
     * This method uses OpenCSV's StatefulBeanToCsv to convert Java beans with CSV
     * annotations
     * into a properly formatted CSV string. It handles quoting, column headers, and
     * separators.
     * </p>
     * 
     * @param <T>   type parameter for the bean class
     * @param beans List of beans with CSV annotations
     * @return CSV string representation of the beans
     * @throws BadRequestException if an error occurs during CSV generation
     */
    private <T> String generateCsvFromBeans(List<T> beans) {
        logger.debug("Starting CSV generation from {} beans", beans.size());

        StringWriter writer = new StringWriter();
        try {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar('"')
                    .withSeparator(',')
                    .build();

            logger.debug("Configured StatefulBeanToCsv for conversion");
            beanToCsv.write(beans);

            String result = writer.toString();
            logger.debug("Successfully completed CSV generation, produced {} characters", result.length());

            return result;
        } catch (CsvDataTypeMismatchException e) {
            logger.error("Data type mismatch error while generating CSV", e);
            throw new BadRequestException(
                    "Failed to generate CSV",
                    "A data type mismatch occurred while processing your export. Please try again or contact support.");
        } catch (CsvRequiredFieldEmptyException e) {
            logger.error("Required field missing while generating CSV", e);
            throw new BadRequestException(
                    "Failed to generate CSV",
                    "A required field is missing for CSV generation. Please try again or contact support.");
        } catch (Exception e) {
            logger.error("Unexpected error while generating CSV", e);
            throw new BadRequestException(
                    "Failed to generate CSV",
                    "An unexpected error occurred while generating the export. Please try again or contact support.");
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all users from the database and exports them to XLSX format.
     * This method generates a comprehensive Excel spreadsheet containing all user
     * information with proper formatting and styling.
     * </p>
     */
    @Override
    public byte[] exportUsersToXLSX(Long requesterId) {
        logger.info("Processing request to export all users to XLSX from user ID: {}", requesterId);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get all users
        List<User> users = daoFactory.forUser().findAll();
        logger.debug("Retrieved {} users for export", users.size());

        // Process and return XLSX
        byte[] result = processUsersToXlsx(users);
        logger.info("Successfully completed XLSX export of all users");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves filtered users from the database and exports them to XLSX format.
     * This method generates a comprehensive Excel spreadsheet containing filtered
     * user
     * information with proper formatting and styling based on the provided filters.
     * </p>
     */
    @Override
    public byte[] exportFilteredUsersToXLSX(Long requesterId, UserFilter filters) {
        logger.info("Processing request to export filtered users to XLSX from user ID: {}", requesterId);
        logger.debug("Applied filters: {}", filters);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get filtered users from the DAO
        List<User> users = daoFactory.forUser().getUsersByFilters(filters);
        logger.debug("Retrieved {} users matching filter criteria", users.size());

        // Process and return XLSX
        byte[] result = processUsersToXlsx(users);
        logger.info("Successfully completed XLSX export of filtered users");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all evaluations from the database and exports them to XLSX format.
     * This method generates a comprehensive Excel spreadsheet containing all
     * evaluation
     * information with proper formatting and styling.
     * </p>
     */
    @Override
    public byte[] exportEvaluationsToXLSX(Long requesterId) {
        logger.info("Processing request to export all evaluations to XLSX from user ID: {}", requesterId);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get all evaluations
        List<Evaluation> evaluations = daoFactory.forEvaluation().findAll();
        logger.debug("Retrieved {} evaluations for export", evaluations.size());

        // Process and return XLSX
        byte[] result = processEvaluationsToXlsx(evaluations);
        logger.info("Successfully completed XLSX export of all evaluations");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves filtered evaluations from the database and exports them to XLSX
     * format.
     * This method generates a comprehensive Excel spreadsheet containing filtered
     * evaluation
     * information with proper formatting and styling based on the provided filters.
     * </p>
     */
    @Override
    public byte[] exportFilteredEvaluationsToXLSX(Long requesterId, EvaluationFilter filters) {
        logger.info("Processing request to export filtered evaluations to XLSX from user ID: {}", requesterId);
        logger.debug("Applied filters: {}", filters);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get filtered evaluations from the DAO
        List<Evaluation> evaluations = daoFactory.forEvaluation().getEvaluationsByFilters(filters);
        logger.debug("Retrieved {} evaluations matching filter criteria", evaluations.size());

        // Process and return XLSX
        byte[] result = processEvaluationsToXlsx(evaluations);
        logger.info("Successfully completed XLSX export of filtered evaluations");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all trainings from the database and exports them to XLSX format.
     * This method generates a comprehensive Excel spreadsheet containing all
     * training
     * information with proper formatting and styling.
     * </p>
     */
    @Override
    public byte[] exportTrainingsToXLSX(Long requesterId) {
        logger.info("Processing request to export all trainings to XLSX from user ID: {}", requesterId);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get all trainings
        List<Training> trainings = daoFactory.forTraining().findAll();
        logger.debug("Retrieved {} trainings for export", trainings.size());

        // Process and return XLSX
        byte[] result = processTrainingsToXlsx(trainings);
        logger.info("Successfully completed XLSX export of all trainings");

        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves filtered trainings from the database and exports them to XLSX
     * format.
     * This method generates a comprehensive Excel spreadsheet containing filtered
     * training
     * information with proper formatting and styling based on the provided filters.
     * </p>
     */
    @Override
    public byte[] exportFilteredTrainingsToXLSX(Long requesterId, TrainingFilter filters) {
        logger.info("Processing request to export filtered trainings to XLSX from user ID: {}", requesterId);
        logger.debug("Applied filters: {}", filters);

        // Verify user exists
        verifyRequester(requesterId);
        logger.debug("Requester validation successful for user ID: {}", requesterId);

        // Get filtered trainings from the DAO
        List<Training> trainings = daoFactory.forTraining().getTrainingsByFilters(filters);
        logger.debug("Retrieved {} trainings matching filter criteria", trainings.size());

        // Process and return XLSX
        byte[] result = processTrainingsToXlsx(trainings);
        logger.info("Successfully completed XLSX export of filtered trainings");

        return result;
    }

    /**
     * Processes a list of users into an XLSX byte array.
     * <p>
     * This method converts User entities to DTOs, then to CSV-specific DTOs,
     * and finally generates an XLSX byte array representation using Apache POI.
     * </p>
     * 
     * @param users List of user entities to convert to XLSX
     * @return XLSX byte array representation of users
     */
    private byte[] processUsersToXlsx(List<User> users) {
        logger.debug("Processing {} users for XLSX export", users.size());

        // Convert to DTOs
        List<UserDTO> userDTOs = converterFactory.forUser().toDTOList(users);
        logger.debug("Converted users to DTOs");

        // Convert to CSV DTOs (we reuse the same structure)
        List<UserCsvDTO> userCsvDTOs = userDTOs.stream()
                .map(UserCsvDTO::new)
                .collect(Collectors.toList());
        logger.debug("Converted user DTOs to CSV-specific DTOs");

        // Generate XLSX
        byte[] result = generateXlsxFromBeans(userCsvDTOs, "Users");
        logger.debug("Generated XLSX byte array with {} bytes", result.length);

        return result;
    }

    /**
     * Processes a list of evaluations into an XLSX byte array.
     * <p>
     * This method converts Evaluation entities to DTOs, then to CSV-specific DTOs,
     * and finally generates an XLSX byte array representation using Apache POI.
     * </p>
     * 
     * @param evaluations List of evaluation entities to convert to XLSX
     * @return XLSX byte array representation of evaluations
     */
    private byte[] processEvaluationsToXlsx(List<Evaluation> evaluations) {
        logger.debug("Processing {} evaluations for XLSX export", evaluations.size());

        // Convert to DTOs
        List<EvaluationDTO> evaluationDTOs = converterFactory.forEvaluation().toDTOList(evaluations);
        logger.debug("Converted evaluations to DTOs");

        // Convert to CSV DTOs (we reuse the same structure)
        List<EvaluationCsvDTO> evaluationCsvDTOs = evaluationDTOs.stream()
                .map(EvaluationCsvDTO::new)
                .collect(Collectors.toList());
        logger.debug("Converted evaluation DTOs to CSV-specific DTOs");

        // Generate XLSX
        byte[] result = generateXlsxFromBeans(evaluationCsvDTOs, "Evaluations");
        logger.debug("Generated XLSX byte array with {} bytes", result.length);

        return result;
    }

    /**
     * Processes a list of trainings into an XLSX byte array.
     * <p>
     * This method converts Training entities to DTOs, then to CSV-specific DTOs,
     * and finally generates an XLSX byte array representation using Apache POI.
     * </p>
     * 
     * @param trainings List of training entities to convert to XLSX
     * @return XLSX byte array representation of trainings
     */
    private byte[] processTrainingsToXlsx(List<Training> trainings) {
        logger.debug("Processing {} trainings for XLSX export", trainings.size());

        // Convert to DTOs
        List<TrainingDTO> trainingDTOs = converterFactory.forTraining().toDTOList(trainings);
        logger.debug("Converted trainings to DTOs");

        // Convert to CSV DTOs (we reuse the same structure)
        List<TrainingCsvDTO> trainingCsvDTOs = trainingDTOs.stream()
                .map(TrainingCsvDTO::new)
                .collect(Collectors.toList());
        logger.debug("Converted training DTOs to CSV-specific DTOs");

        // Generate XLSX
        byte[] result = generateXlsxFromBeans(trainingCsvDTOs, "Trainings");
        logger.debug("Generated XLSX byte array with {} bytes", result.length);

        return result;
    }

    /**
     * Generates an XLSX byte array from a list of beans using Apache POI.
     * <p>
     * This method uses Apache POI's XSSFWorkbook to convert Java beans with CSV
     * annotations into a properly formatted XLSX file. It handles headers, data
     * types, and basic formatting.
     * </p>
     * 
     * @param <T>       type parameter for the bean class
     * @param beans     List of beans with CSV annotations
     * @param sheetName Name of the worksheet
     * @return XLSX byte array representation of the beans
     * @throws BadRequestException if an error occurs during XLSX generation
     */
    private <T> byte[] generateXlsxFromBeans(List<T> beans, String sheetName) {
        logger.debug("Starting XLSX generation from {} beans for sheet: {}", beans.size(), sheetName);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            if (beans.isEmpty()) {
                logger.warn("No data to export for sheet: {}", sheetName);
                workbook.write(baos);
                return baos.toByteArray();
            }

            // Create header style
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Create data style
            XSSFCellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            // Use reflection to get field names from CSV annotations
            T firstBean = beans.get(0);
            java.lang.reflect.Field[] fields = firstBean.getClass().getDeclaredFields();

            // Create header row
            Row headerRow = sheet.createRow(0);
            int headerIndex = 0;

            for (java.lang.reflect.Field field : fields) {
                com.opencsv.bean.CsvBindByName csvAnnotation = field
                        .getAnnotation(com.opencsv.bean.CsvBindByName.class);
                if (csvAnnotation != null) {
                    org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(headerIndex++);
                    cell.setCellValue(csvAnnotation.column());
                    cell.setCellStyle(headerStyle);
                }
            }

            // Fill data rows
            for (int i = 0; i < beans.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                T bean = beans.get(i);
                int cellIndex = 0;

                for (java.lang.reflect.Field field : fields) {
                    com.opencsv.bean.CsvBindByName csvAnnotation = field
                            .getAnnotation(com.opencsv.bean.CsvBindByName.class);
                    if (csvAnnotation != null) {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(bean);
                            org.apache.poi.ss.usermodel.Cell cell = dataRow.createCell(cellIndex++);

                            if (value != null) {
                                if (value instanceof Number) {
                                    cell.setCellValue(((Number) value).doubleValue());
                                } else if (value instanceof Boolean) {
                                    cell.setCellValue((Boolean) value);
                                } else if (value instanceof java.time.LocalDate) {
                                    cell.setCellValue(value.toString());
                                } else if (value instanceof java.time.LocalDateTime) {
                                    cell.setCellValue(value.toString());
                                } else {
                                    cell.setCellValue(value.toString());
                                }
                            } else {
                                cell.setCellValue("");
                            }
                            cell.setCellStyle(dataStyle);
                        } catch (IllegalAccessException e) {
                            logger.warn("Could not access field {} for bean {}", field.getName(),
                                    bean.getClass().getSimpleName());
                            org.apache.poi.ss.usermodel.Cell cell = dataRow.createCell(cellIndex++);
                            cell.setCellValue("");
                            cell.setCellStyle(dataStyle);
                        }
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < headerIndex; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            byte[] result = baos.toByteArray();
            logger.debug("Successfully completed XLSX generation, produced {} bytes", result.length);

            return result;
        } catch (Exception e) {
            logger.error("Unexpected error while generating XLSX", e);
            throw new BadRequestException(
                    "Failed to generate XLSX",
                    "An unexpected error occurred while generating the XLSX export. Please try again or contact support.");
        }
    }

}
