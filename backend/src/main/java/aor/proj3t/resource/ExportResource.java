package aor.proj3t.resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
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
}
