package aor.proj3t.util.validatorutils.fordto;

import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.exceptions.InvalidDataException;
import aor.proj3t.filters.DateRange;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.regex.Pattern;

@ApplicationScoped
public class EvaluationValidator {


    public EvaluationValidator() {
        // Default constructor
    }

    public void validateDateRange(DateRange dateRange) throws InvalidDataException {

        Instant start = dateRange.getStartDate();
        Instant end = dateRange.getEndDate();
        if(start != null && end == null && start.isAfter(Instant.now())) {
            throw new InvalidDataException("startDate must be before or equal to the current time");
        }

        if ( start != null && end != null && start.isAfter(end)) {
            throw new InvalidDataException("startDate must be before or equal to endDate");
        }
        if(end != null && end.isAfter(Instant.now())) {
            throw new InvalidDataException("Date range cannot be in the future");
        }
    }


    public void validateTrainingTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException("Title is required");
        }
        if (title.length() > 100) {
            throw new InvalidDataException("Title must not exceed 100 characters");
        }
    }

    public void validateTrainingDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new InvalidDataException("Duration must be a positive number of minutes");
        }
    }

    public void validateTrainingDescription(String description) {
        if (description == null || description.length() < 10 || description.length() > 1000) {
            throw new InvalidDataException("Description must be between 10 and 1000 characters");
        }
    }

    public void validateTrainingLink(String link) {
        if (link != null && !link.isEmpty()) {
            if (link.length() > 500) {
                throw new InvalidDataException("Link must not exceed 500 characters");
            }
            String urlRegex = "^(https?|ftp)://[\\w.-]+(?:\\.[\\w.-]+)+(?:[/?#]\\S*)?$";
            if (!Pattern.matches(urlRegex, link)) {
                throw new InvalidDataException("Link is not a valid URL format");
            }
        }
    }

    public void validateTrainingLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new InvalidDataException("Language is required");
        }
    }


    public void newCycleDateRangeValidation(DateRange dateRange) throws InvalidDataException {
        if (dateRange == null) {
            throw new InvalidDataException("Date range must not be null");
        }
        if(dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
            throw new InvalidDataException(
                    "start date and end date are required",
                    "Evaluation cycle started without start and end dates");
        }
        if(dateRange.getStartDate().isAfter(dateRange.getEndDate())) {
            throw new InvalidDataException(
                    "start date must be before end date",
                    "Evaluation cycle started without start end after end date");
        }
        if(dateRange.getStartDate().isBefore(Instant.now())) {
            throw new InvalidDataException(
                    "start date should be after current time",
                    "Evaluation cycle started without start date previous to current time");
        }
    }
}