package aor.proj3t.service.impl;

import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.UserCsvDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.User;
import aor.proj3t.exceptions.BadRequestException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.interfaces.ExportService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of ExportService that provides functionality for exporting
 * data
 * to various formats.
 * <p>
 * This service handles the export of system data to CSV format.
 * Currently supports user data export with optional filtering capabilities.
 * Uses OpenCSV for bean-to-CSV conversion.
 * </p>
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
        String result = generateUsersCsvFromBeans(userCsvDTOs);
        logger.debug("Generated CSV string with {} characters", result.length());

        return result;
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
    private <T> String generateUsersCsvFromBeans(List<T> beans) {
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
}
