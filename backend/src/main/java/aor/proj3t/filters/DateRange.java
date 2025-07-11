package aor.proj3t.filters;

import java.time.Instant;

import jakarta.ws.rs.QueryParam;

/**
 * Date Range Filter for Query Parameters.
 * 
 * This class represents a date range filter that can be used in REST API
 * endpoints
 * to filter results based on a start and end date. It automatically binds to
 * "startDate" and "endDate" query parameters.
 * 
 * <p>
 * Example usage in a REST endpoint:
 * </p>
 * 
 * <pre>
 * GET /api/trainings?startDate=2025-01-01T00:00:00Z&endDate=2025-12-31T23:59:59Z
 * </pre>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class DateRange {

    /**
     * The start date of the range (inclusive).
     */
    @QueryParam("startDate")
    private Instant startDate;

    /**
     * The end date of the range (inclusive).
     */
    @QueryParam("endDate")
    private Instant endDate;

    /**
     * Constructs a DateRange with specified start and end dates.
     * 
     * @param startDate the start date of the range (inclusive), may be null
     * @param endDate   the end date of the range (inclusive), may be null
     */
    public DateRange(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Default constructor for JAX-RS parameter binding.
     */
    public DateRange() {
    }

    /**
     * Gets the start date of the range.
     * 
     * @return the start date, or null if not specified
     */
    public Instant getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the range.
     * 
     * @param startDate the start date to set, may be null
     */
    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the range.
     * 
     * @return the end date, or null if not specified
     */
    public Instant getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the range.
     * 
     * @param endDate the end date to set, may be null
     */
    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }
}
