package aor.proj3t.resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource handler for exporting data in various formats.
 * <p>
 * This resource provides endpoints for exporting system data into formats such
 * as CSV.
 * Currently supports user data exports with optional filtering capabilities.
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 */
@Path("/export")
@RequestScoped
public class ExportResource {
    private static final Logger logger = LogManager.getLogger(ExportResource.class);

    @Inject
    ServiceFactory serviceFactory;

    /**
     * Exports all users in the system to a CSV file.
     * <p>
     * This endpoint generates a CSV file containing all users in the system.
     * The CSV includes user details such as ID, email, name, role, etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @return a Response with the CSV data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/users/csv")
    @Secured
    @Produces("text/csv")
    public Response exportUsersToCSV(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to export all users to CSV");

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        String csvData = serviceFactory.forExport().exportUsersToCSV(requesterId);
        logger.info("Successfully generated CSV export for all users");

        Response response = createCsvResponse(csvData, "users_export");
        logger.debug("Returning CSV response with {} bytes of data", csvData.length());

        return response;
    }

    /**
     * Exports filtered users to a CSV file.
     * <p>
     * This endpoint generates a CSV file containing users that match the specified
     * filters.
     * Filters can be applied to various user properties such as role, workplace,
     * etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @param filters    a bean containing filter criteria to apply to the user list
     * @return a Response with the CSV data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/users/csv/filtered")
    @Secured
    @Produces("text/csv")
    public Response exportFilteredUsersToCSV(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @BeanParam UserFilter filters) {
        logger.info("Received request to export filtered users to CSV");
        logger.debug("Applied filters: {}", filters);

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        String csvData = serviceFactory.forExport().exportFilteredUsersToCSV(requesterId, filters);
        logger.info("Successfully generated CSV export for filtered users");

        Response response = createCsvResponse(csvData, "filtered_users_export");
        logger.debug("Returning CSV response with {} bytes of data", csvData.length());

        return response;
    }

    /**
     * Exports all evaluations in the system to a CSV file.
     * <p>
     * This endpoint generates a CSV file containing all evaluations in the system.
     * The CSV includes evaluation details such as ID, scale, status, feedback, etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @return a Response with the CSV data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/evaluations/csv")
    @Secured
    @Produces("text/csv")
    public Response exportEvaluationsToCSV(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to export all evaluations to CSV");

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        String csvData = serviceFactory.forExport().exportEvaluationsToCSV(requesterId);
        logger.info("Successfully generated CSV export for all evaluations");

        Response response = createCsvResponse(csvData, "evaluations_export");
        logger.debug("Returning CSV response with {} bytes of data", csvData.length());

        return response;
    }

    /**
     * Exports filtered evaluations to a CSV file.
     * <p>
     * This endpoint generates a CSV file containing evaluations that match the
     * specified
     * filters.
     * Filters can be applied to various evaluation properties such as status,
     * scale,
     * date range, etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @param filters    a bean containing filter criteria to apply to the
     *                   evaluation list
     * @return a Response with the CSV data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/evaluations/csv/filtered")
    @Secured
    @Produces("text/csv")
    public Response exportFilteredEvaluationsToCSV(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @BeanParam EvaluationFilter filters) {
        logger.info("Received request to export filtered evaluations to CSV");
        logger.debug("Applied filters: {}", filters);

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        // Ensure filter date range is hydrated if provided
        if (filters.getStartDate() != null || filters.getEndDate() != null) {
            filters.hydrate();
        }

        String csvData = serviceFactory.forExport().exportFilteredEvaluationsToCSV(requesterId, filters);
        logger.info("Successfully generated CSV export for filtered evaluations");

        Response response = createCsvResponse(csvData, "filtered_evaluations_export");
        logger.debug("Returning CSV response with {} bytes of data", csvData.length());

        return response;
    }

    /**
     * Exports a single evaluation to a PDF file with detailed information.
     * <p>
     * This endpoint generates a PDF document containing comprehensive details about
     * a specific evaluation, including information about the evaluator, evaluated
     * user,
     * and the evaluation itself.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader   the authorization header containing the JWT token
     * @param evaluationId the ID of the evaluation to export
     * @return a Response with the PDF data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/evaluations/{id}/pdf")
    @Secured
    @Produces("application/pdf")
    public Response exportEvaluationToPDF(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("id") Long evaluationId) {
        logger.info("Received request to export evaluation ID: {} to PDF", evaluationId);

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        byte[] pdfData = serviceFactory.forExport().exportEvaluationToPDF(requesterId, evaluationId);
        logger.info("Successfully generated PDF export for evaluation ID: {}", evaluationId);

        // Generate filename with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "evaluation_" + evaluationId + "_" + timestamp + ".pdf";
        logger.debug("Generated filename for export: {}", filename);

        return Response.ok(pdfData, MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .build();
    }

    /**
     * Exports all trainings in the system to a CSV file.
     * <p>
     * This endpoint generates a CSV file containing all trainings in the system.
     * The CSV includes training details such as ID, title, duration, description,
     * etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @return a Response with the CSV data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/trainings/csv")
    @Secured
    @Produces("text/csv")
    public Response exportTrainingsToCSV(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to export all trainings to CSV");

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        String csvData = serviceFactory.forExport().exportTrainingsToCSV(requesterId);
        logger.info("Successfully generated CSV export for all trainings");

        Response response = createCsvResponse(csvData, "trainings_export");
        logger.debug("Returning CSV response with {} bytes of data", csvData.length());

        return response;
    }

    /**
     * Exports filtered trainings to a CSV file.
     * <p>
     * This endpoint generates a CSV file containing trainings that match the
     * specified
     * filters.
     * Filters can be applied to various training properties such as training area,
     * language,
     * duration, etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @param filters    a bean containing filter criteria to apply to the training
     *                   list
     * @return a Response with the CSV data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/trainings/csv/filtered")
    @Secured
    @Produces("text/csv")
    public Response exportFilteredTrainingsToCSV(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @BeanParam TrainingFilter filters) {
        logger.info("Received request to export filtered trainings to CSV");
        logger.debug("Applied filters: {}", filters);

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        // Ensure filter date is hydrated if provided
        if (filters.getStartDate() != null) {
            filters.hydrate();
        }

        String csvData = serviceFactory.forExport().exportFilteredTrainingsToCSV(requesterId, filters);
        logger.info("Successfully generated CSV export for filtered trainings");

        Response response = createCsvResponse(csvData, "filtered_trainings_export");
        logger.debug("Returning CSV response with {} bytes of data", csvData.length());

        return response;
    }

    // XLSX Export Endpoints

    /**
     * Exports all users in the system to an XLSX file.
     * <p>
     * This endpoint generates an XLSX file containing all users in the system.
     * The XLSX includes user details such as ID, email, name, role, etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @return a Response with the XLSX data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/users/xlsx")
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportUsersToXLSX(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to export all users to XLSX");

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        byte[] xlsxData = serviceFactory.forExport().exportUsersToXLSX(requesterId);
        logger.info("Successfully generated XLSX export for all users");

        Response response = createXlsxResponse(xlsxData, "users_export");
        logger.debug("Returning XLSX response with {} bytes of data", xlsxData.length);

        return response;
    }

    /**
     * Exports filtered users to an XLSX file.
     * <p>
     * This endpoint generates an XLSX file containing users that match the
     * specified
     * filters.
     * Filters can be applied to various user properties such as role, workplace,
     * etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @param filters    a bean containing filter criteria to apply to the user list
     * @return a Response with the XLSX data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/users/xlsx/filtered")
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportFilteredUsersToXLSX(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @BeanParam UserFilter filters) {
        logger.info("Received request to export filtered users to XLSX");
        logger.debug("Applied filters: {}", filters);

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        byte[] xlsxData = serviceFactory.forExport().exportFilteredUsersToXLSX(requesterId, filters);
        logger.info("Successfully generated XLSX export for filtered users");

        Response response = createXlsxResponse(xlsxData, "users_filtered_export");
        logger.debug("Returning XLSX response with {} bytes of data", xlsxData.length);

        return response;
    }

    /**
     * Exports all evaluations in the system to an XLSX file.
     * <p>
     * This endpoint generates an XLSX file containing all evaluations in the
     * system.
     * The XLSX includes evaluation details such as ID, scale, status, feedback,
     * etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @return a Response with the XLSX data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/evaluations/xlsx")
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportEvaluationsToXLSX(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to export all evaluations to XLSX");

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        byte[] xlsxData = serviceFactory.forExport().exportEvaluationsToXLSX(requesterId);
        logger.info("Successfully generated XLSX export for all evaluations");

        Response response = createXlsxResponse(xlsxData, "evaluations_export");
        logger.debug("Returning XLSX response with {} bytes of data", xlsxData.length);

        return response;
    }

    /**
     * Exports filtered evaluations to an XLSX file.
     * <p>
     * This endpoint generates an XLSX file containing evaluations that match the
     * specified filters.
     * Filters can be applied to various evaluation properties such as status,
     * scale,
     * date range, etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @param filters    a bean containing filter criteria to apply to the
     *                   evaluation list
     * @return a Response with the XLSX data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/evaluations/xlsx/filtered")
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportFilteredEvaluationsToXLSX(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @BeanParam EvaluationFilter filters) {
        logger.info("Received request to export filtered evaluations to XLSX");
        logger.debug("Applied filters: {}", filters);

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        byte[] xlsxData = serviceFactory.forExport().exportFilteredEvaluationsToXLSX(requesterId, filters);
        logger.info("Successfully generated XLSX export for filtered evaluations");

        Response response = createXlsxResponse(xlsxData, "evaluations_filtered_export");
        logger.debug("Returning XLSX response with {} bytes of data", xlsxData.length);

        return response;
    }

    /**
     * Exports all trainings in the system to an XLSX file.
     * <p>
     * This endpoint generates an XLSX file containing all trainings in the system.
     * The XLSX includes training details such as ID, title, duration, description,
     * etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @return a Response with the XLSX data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/trainings/xlsx")
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportTrainingsToXLSX(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to export all trainings to XLSX");

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        byte[] xlsxData = serviceFactory.forExport().exportTrainingsToXLSX(requesterId);
        logger.info("Successfully generated XLSX export for all trainings");

        Response response = createXlsxResponse(xlsxData, "trainings_export");
        logger.debug("Returning XLSX response with {} bytes of data", xlsxData.length);

        return response;
    }

    /**
     * Exports filtered trainings to an XLSX file.
     * <p>
     * This endpoint generates an XLSX file containing trainings that match the
     * specified filters.
     * Filters can be applied to various training properties such as training area,
     * language, duration, etc.
     * The endpoint requires authentication.
     * </p>
     *
     * @param authHeader the authorization header containing the JWT token
     * @param filters    a bean containing filter criteria to apply to the training
     *                   list
     * @return a Response with the XLSX data and appropriate headers for file
     *         download
     */
    @GET
    @Path("/trainings/xlsx/filtered")
    @Secured
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportFilteredTrainingsToXLSX(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @BeanParam TrainingFilter filters) {
        logger.info("Received request to export filtered trainings to XLSX");
        logger.debug("Applied filters: {}", filters);

        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        logger.debug("Request initiated by user with ID: {}", requesterId);

        byte[] xlsxData = serviceFactory.forExport().exportFilteredTrainingsToXLSX(requesterId, filters);
        logger.info("Successfully generated XLSX export for filtered trainings");

        Response response = createXlsxResponse(xlsxData, "trainings_filtered_export");
        logger.debug("Returning XLSX response with {} bytes of data", xlsxData.length);

        return response;
    }

    /**
     * Creates a CSV response with proper headers and filename.
     * <p>
     * This helper method prepares the Response object with the appropriate headers
     * for a CSV file download, including a dynamically generated filename with
     * timestamp.
     * </p>
     * 
     * @param csvData        the CSV content to return in the response body
     * @param filenamePrefix prefix for the generated filename
     * @return Response object with CSV content and appropriate headers
     */
    private Response createCsvResponse(String csvData, String filenamePrefix) {
        // Generate a filename with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = filenamePrefix + "_" + timestamp + ".csv";

        logger.debug("Generated filename for export: {}", filename);

        return Response.ok(csvData)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .build();
    }

    /**
     * Creates an XLSX response with proper headers and filename.
     * <p>
     * This helper method prepares the Response object with the appropriate headers
     * for an XLSX file download, including a dynamically generated filename with
     * timestamp.
     * </p>
     * 
     * @param xlsxData       the XLSX content to return in the response body
     * @param filenamePrefix prefix for the generated filename
     * @return Response object with XLSX content and appropriate headers
     */
    private Response createXlsxResponse(byte[] xlsxData, String filenamePrefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = filenamePrefix + "_" + timestamp + ".xlsx";

        return Response.ok(xlsxData)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .build();
    }
}
