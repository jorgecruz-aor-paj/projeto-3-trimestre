package aor.proj3t.filters;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationSortField;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.exceptions.BadRequestException;
import jakarta.ws.rs.QueryParam;

/**
 * Filter class for evaluation queries.
 * 
 * This class encapsulates all possible filter criteria for searching and
 * filtering
 * evaluations in the system. It supports filtering by evaluation participants,
 * status, dates, evaluation scales, and text search, as well as sorting
 * options.
 * 
 * <p>
 * The filter supports both single date filtering and date range filtering.
 * Date strings are automatically parsed and validated during the hydration
 * process.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * GET /api/evaluations?evaluatedId=123&status=COMPLETED&startDate=2025-01-01T00:00:00Z
 * </pre>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class EvaluationFilter {

    /**
     * Filter by specific evaluation ID.
     */
    @QueryParam("evaluationId")
    private Long evaluationId;

    /**
     * Filter by the ID of the user being evaluated.
     */
    @QueryParam("evaluatedId")
    private Long evaluatedId;

    /**
     * Filter by the ID of the user performing the evaluation.
     */
    @QueryParam("evaluatorId")
    private Long evaluatorId;

    /**
     * Filter by evaluation status.
     */
    @QueryParam("status")
    private EvaluationStatus evaluationStatus;

    /**
     * Single date filter as string (format: yyyy-MM-dd).
     */
    @QueryParam("date")
    private String dateString;

    /**
     * Filter by evaluation scale/rating.
     */
    @QueryParam("evaluationScale")
    private EvaluationScale evaluationScale;

    /**
     * Field to sort evaluations by. Defaults to DATE.
     */
    @QueryParam("evaluationSortField")
    private EvaluationSortField evaluationSortField = EvaluationSortField.DATE;

    /**
     * Filter by whether the evaluation cycle is closed. Defaults to false.
     */
    @QueryParam("cycleClosed")
    private Boolean cycleClosed = false;

    /**
     * Sort order: true for ascending, false for descending. Defaults to false.
     */
    @QueryParam("ascending")
    private Boolean ascending = false;

    /**
     * Text search across evaluation content.
     */
    @QueryParam("searchText")
    private String searchText;

    /**
     * Start date for date range filtering (ISO-8601 format).
     */
    @QueryParam("startDate")
    private String startDate;

    /**
     * End date for date range filtering (ISO-8601 format).
     */
    @QueryParam("endDate")
    private String endDate;

    /**
     * Parsed single date from dateString.
     */
    private LocalDate date;

    /**
     * Parsed date range from startDate and endDate.
     */
    private DateRange dateRange;

    /**
     * Default constructor for JAX-RS parameter binding.
     */
    public EvaluationFilter() {
    }

    /**
     * Gets the evaluation ID filter.
     * 
     * @return the evaluation ID, or null if not specified
     */
    public Long getEvaluationId() {
        return evaluationId;
    }

    /**
     * Sets the evaluation ID filter.
     * 
     * @param evaluationId the evaluation ID to filter by
     */
    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    /**
     * Gets the evaluated user ID filter.
     * 
     * @return the evaluated user ID, or null if not specified
     */
    public Long getEvaluatedId() {
        return evaluatedId;
    }

    /**
     * Sets the evaluated user ID filter.
     * 
     * @param evaluatedId the evaluated user ID to filter by
     */
    public void setEvaluatedId(Long evaluatedId) {
        this.evaluatedId = evaluatedId;
    }

    /**
     * Gets the evaluator user ID filter.
     * 
     * @return the evaluator user ID, or null if not specified
     */
    public Long getEvaluatorId() {
        return evaluatorId;
    }

    /**
     * Sets the evaluator user ID filter.
     * 
     * @param evaluatorId the evaluator user ID to filter by
     */
    public void setEvaluatorId(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    /**
     * Gets the evaluation status filter.
     * 
     * @return the evaluation status, or null if not specified
     */
    public EvaluationStatus getEvaluationStatus() {
        return evaluationStatus;
    }

    /**
     * Sets the evaluation status filter.
     * 
     * @param evaluationStatus the evaluation status to filter by
     */
    public void setEvaluationStatus(EvaluationStatus evaluationStatus) {
        this.evaluationStatus = evaluationStatus;
    }

    /**
     * Gets the parsed single date filter.
     * 
     * @return the parsed date, or null if not specified
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the parsed single date filter.
     * 
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the evaluation scale filter.
     * 
     * @return the evaluation scale, or null if not specified
     */
    public EvaluationScale getEvaluationScale() {
        return evaluationScale;
    }

    /**
     * Sets the evaluation scale filter.
     * 
     * @param evaluationScale the evaluation scale to filter by
     */
    public void setEvaluationScale(EvaluationScale evaluationScale) {
        this.evaluationScale = evaluationScale;
    }

    /**
     * Gets the sort field for evaluations.
     * 
     * @return the sort field, defaults to DATE
     */
    public EvaluationSortField getEvaluationSortField() {
        return evaluationSortField;
    }

    /**
     * Sets the sort field for evaluations.
     * 
     * @param evaluationSortField the sort field to use
     */
    public void setEvaluationSortField(EvaluationSortField evaluationSortField) {
        this.evaluationSortField = evaluationSortField;
    }

    /**
     * Gets the sort order.
     * 
     * @return true for ascending order, false for descending
     */
    public Boolean getAscending() {
        return ascending;
    }

    /**
     * Sets the sort order.
     * 
     * @param ascending true for ascending order, false for descending
     */
    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Sets the end date string for date range filtering.
     * 
     * @param endDate the end date string in ISO-8601 format
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the end date string for date range filtering.
     * 
     * @return the end date string, or null if not specified
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the start date string for date range filtering.
     * 
     * @param startDate the start date string in ISO-8601 format
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the start date string for date range filtering.
     * 
     * @return the start date string, or null if not specified
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets the parsed date range filter.
     * 
     * @return the date range, or null if not specified
     */
    public DateRange getDateRange() {
        return dateRange;
    }

    /**
     * Sets the parsed date range filter.
     * 
     * @param dateRange the date range to set
     */
    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
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
     * @param searchText the text to search for in evaluations
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * Gets the single date string filter.
     * 
     * @return the date string in yyyy-MM-dd format, or null if not specified
     */
    public String getDateString() {
        return dateString;
    }

    /**
     * Sets the single date string filter.
     * 
     * @param dateString the date string in yyyy-MM-dd format
     */
    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    /**
     * Gets the cycle closed filter.
     * 
     * @return true to filter only closed cycles, false otherwise
     */
    public Boolean getCycleClosed() {
        return cycleClosed;
    }

    /**
     * Sets the cycle closed filter.
     * 
     * @param cycleClosed true to filter only closed cycles, false otherwise
     */
    public void setCycleClosed(Boolean cycleClosed) {
        this.cycleClosed = cycleClosed;
    }

    /**
     * Parses and validates string date parameters into proper date objects.
     * 
     * <p>
     * This method converts:
     * </p>
     * <ul>
     * <li>dateString (yyyy-MM-dd format) to LocalDate</li>
     * <li>startDate and endDate (ISO-8601 format) to DateRange with Instant
     * values</li>
     * </ul>
     * 
     * <p>
     * If only startDate is provided, endDate defaults to current time.
     * If only endDate is provided, startDate defaults to 100 years ago.
     * </p>
     * 
     * @throws BadRequestException if date parsing fails due to invalid format
     */

    public void hydrate() {
        // Hydrate the single date (from dateString to LocalDate)
        if (dateString != null && !dateString.isEmpty()) {
            try {
                this.date = LocalDate.parse(dateString); // default format: yyyy-MM-dd
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Invalid date format. Expected yyyy-MM-dd.");
            }
        }

        // Hydrate the DateRange (from Strings to Instant range)
        if (startDate != null || endDate != null) {
            Instant start = null;
            Instant end = null;

            try {
                if (startDate != null && !startDate.isEmpty()) {
                    start = Instant.parse(startDate); // ISO-8601 format expected (e.g., 2025-06-23T08:30:00Z)
                    if (endDate == null || endDate.isEmpty()) {
                        end = Instant.now();
                    }
                }
                if (endDate != null && !endDate.isEmpty()) {
                    end = Instant.parse(endDate);
                    if (start == null) {
                        start = Instant.now().minus(365 * 100, ChronoUnit.DAYS); // 100 years ago
                    }
                }
            } catch (DateTimeParseException e) {
                throw new BadRequestException(
                        "Invalid startDate or endDate format. Use ISO-8601 format like 2025-06-23T08:30:00Z.");
            }

            this.dateRange = new DateRange(start, end);
        }
    }

    @Override
    public String toString() {
        return "EvaluationFilter{" +
                "evaluationId=" + evaluationId +
                ", evaluatedId=" + evaluatedId +
                ", evaluatorId=" + evaluatorId +
                ", evaluationStatus=" + evaluationStatus +
                ", dateString='" + dateString + '\'' +
                ", evaluationScale=" + evaluationScale +
                ", evaluationSortField=" + evaluationSortField +
                ", ascending=" + ascending +
                ", searchText='" + searchText + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", date=" + date +
                ", dateRange=" + dateRange +
                '}';
    }
}
