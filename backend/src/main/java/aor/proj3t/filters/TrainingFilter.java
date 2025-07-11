package aor.proj3t.filters;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import aor.proj3t.enums.TrainingLanguage;
import aor.proj3t.enums.TrainingSortField;
import aor.proj3t.exceptions.BadRequestException;
import jakarta.ws.rs.QueryParam;

/**
 * Filter class for training queries.
 * 
 * This class encapsulates all possible filter criteria for searching and
 * filtering
 * training sessions in the system. It supports filtering by training area,
 * language,
 * duration, date, and text search, as well as sorting options.
 * 
 * <p>
 * The filter includes options to show only active trainings and provides
 * flexible sorting by various training fields.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * GET /api/trainings?trainingAreaId=5&language=ENGLISH&activeOnly=true&sortField=DATE
 * </pre>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class TrainingFilter {

    /**
     * Filter by specific training ID.
     */
    @QueryParam("id")
    private Long id;

    /**
     * Filter by training area ID.
     */
    @QueryParam("trainingAreaId")
    private Long trainingAreaId;

    /**
     * Filter by training language.
     */
    @QueryParam("language")
    private TrainingLanguage language;

    /**
     * Filter by training duration in minutes.
     */
    @QueryParam("duration")
    private Integer duration;

    /**
     * Start date filter as string (ISO-8601 format).
     */
    @QueryParam("startDate")
    private String startDate;

    /**
     * Filter to show only active trainings. Defaults to true.
     */
    @QueryParam("activeOnly")
    private Boolean activeOnly = true;

    /**
     * Field to sort trainings by. Defaults to DATE.
     */
    @QueryParam("sortField")
    private TrainingSortField sortField = TrainingSortField.DATE;

    /**
     * Sort order: true for ascending, false for descending. Defaults to false.
     */
    @QueryParam("sortAscending")
    private Boolean sortAscending = false;

    /**
     * Text search across training content.
     */
    @QueryParam("searchText")
    private String searchText;

    /**
     * Parsed start date from startDate string.
     */
    private Instant startDateInstant;

    /**
     * Default constructor for JAX-RS parameter binding.
     */
    public TrainingFilter() {
    }

    /**
     * Gets the training ID filter.
     * 
     * @return the training ID, or null if not specified
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the training ID filter.
     * 
     * @param id the training ID to filter by
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the training area ID filter.
     * 
     * @return the training area ID, or null if not specified
     */
    public Long getTrainingAreaId() {
        return trainingAreaId;
    }

    /**
     * Sets the training area ID filter.
     * 
     * @param trainingAreaId the training area ID to filter by
     */
    public void setTrainingAreaId(Long trainingAreaId) {
        this.trainingAreaId = trainingAreaId;
    }

    /**
     * Gets the training language filter.
     * 
     * @return the training language, or null if not specified
     */
    public TrainingLanguage getLanguage() {
        return language;
    }

    /**
     * Sets the training language filter.
     * 
     * @param language the training language to filter by
     */
    public void setLanguage(TrainingLanguage language) {
        this.language = language;
    }

    /**
     * Gets the training duration filter.
     * 
     * @return the duration in minutes, or null if not specified
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets the training duration filter.
     * 
     * @param duration the duration in minutes to filter by
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Gets the start date string filter.
     * 
     * @return the start date string in ISO-8601 format, or null if not specified
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date string filter.
     * 
     * @param startDate the start date string in ISO-8601 format
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the sort field for trainings.
     * 
     * @return the sort field, defaults to DATE
     */
    public TrainingSortField getSortField() {
        return sortField;
    }

    /**
     * Sets the sort field for trainings.
     * 
     * @param sortField the sort field to use
     */
    public void setSortField(TrainingSortField sortField) {
        this.sortField = sortField;
    }

    /**
     * Gets the sort order.
     * 
     * @return true for ascending order, false for descending
     */
    public Boolean getSortAscending() {
        return sortAscending;
    }

    /**
     * Sets the sort order.
     * 
     * @param sortAscending true for ascending order, false for descending
     */
    public void setSortAscending(Boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    /**
     * Gets the search text filter.
     * 
     * @return the search text, or null if not specified
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * Sets the search text filter.
     * 
     * @param searchText the text to search for in trainings
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * Gets the active only filter.
     * 
     * @return true to show only active trainings, false to show all
     */
    public Boolean getActiveOnly() {
        return activeOnly;
    }

    /**
     * Sets the active only filter.
     * 
     * @param activeOnly true to show only active trainings, false to show all
     */
    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    /**
     * Gets the parsed start date instant.
     * 
     * @return the parsed start date, or null if not specified
     */
    public Instant getStartDateInstant() {
        return startDateInstant;
    }

    /**
     * Sets the parsed start date instant.
     * 
     * @param startDateInstant the start date instant to set
     */
    public void setStartDateInstant(Instant startDateInstant) {
        this.startDateInstant = startDateInstant;
    }

    /**
     * Parses and validates string date parameters into proper date objects.
     * 
     * <p>
     * This method converts the startDate string (ISO-8601 format) to an Instant.
     * It also ensures that sortAscending has a default value if not specified.
     * </p>
     * 
     * @throws BadRequestException if date parsing fails due to invalid format
     */

    public void hydrate() {
        if (startDate != null && !startDate.isEmpty()) {
            try {
                this.startDateInstant = Instant.parse(startDate); // expecting ISO-8601 format like
                                                                  // "2025-06-23T08:30:00Z"
            } catch (DateTimeParseException e) {
                throw new BadRequestException(
                        "Invalid startDate or endDate format. Use ISO-8601 format like 2025-06-23T08:30:00Z.");
            }
        }
        if (this.sortAscending == null) {
            this.sortAscending = false;
        }

    }

    @Override
    public String toString() {
        return "TrainingFilter{" +
                "id=" + id +
                ", trainingAreaId=" + trainingAreaId +
                ", language='" + language + '\'' +
                ", duration=" + duration +
                ", startDate='" + startDate + '\'' +
                ", sortField=" + sortField +
                ", sortAscending=" + sortAscending +
                ", searchText='" + searchText + '\'' +
                ", activeOnly=" + activeOnly +
                '}';
    }
}
