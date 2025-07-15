package aor.proj3t.dto;

/**
 * Data Transfer Object for representing training statistics per year.
 * <p>
 * This lightweight DTO holds aggregate training duration metrics tied to a specific calendar year.
 * Often used in analytics views, reports, or chart rendering for training engagement over time.
 * </p>
 *
 * Attributes:
 * - {@code year}: Calendar year for which the data is collected
 * - {@code minutes}: Total number of training minutes logged in that year
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class TrainingYearStatDTO {

    /** Year the training statistics apply to */
    public int year;

    /** Total number of minutes of training logged in this year */
    public long minutes;

    public TrainingYearStatDTO(int year, long minutes) {
        this.year = year;
        this.minutes = minutes;
    }

    public int getYear() {
        return year;
    }

    public long getMinutes() {
        return minutes;
    }
}

