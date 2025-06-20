package aor.proj3t.filters;

import java.time.Instant;

import jakarta.ws.rs.QueryParam;

public class DateRange {
    @QueryParam("startDate")
    private Instant startDate;
    @QueryParam("endDate")
    private Instant endDate;

    public DateRange(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange() {
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

}
