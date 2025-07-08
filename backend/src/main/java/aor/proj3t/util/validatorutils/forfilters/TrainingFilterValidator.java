package aor.proj3t.util.validatorutils.forfilters;

import aor.proj3t.enums.TrainingLanguage;
import aor.proj3t.enums.TrainingSortField;
import aor.proj3t.filters.TrainingFilter;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Validator for TrainingFilter objects.
 * Validates all fields in TrainingFilter according to business rules.
 */
@ApplicationScoped
public class TrainingFilterValidator extends BaseFilterValidator<TrainingFilter> {

    private static final int MAX_SEARCH_TEXT_LENGTH = 255;
    private static final int MIN_DURATION = 1;
    private static final int MAX_DURATION = 1000;

    @Override
    protected void doValidate(TrainingFilter filter, List<String> validationErrors) {
        // Manual validations equivalent to annotations
        validateId(filter, validationErrors);
        validateTrainingAreaId(filter, validationErrors);
        validateLanguage(filter, validationErrors);
        validateDuration(filter, validationErrors);
        validateStartDate(filter, validationErrors);
        validateSearchText(filter, validationErrors);
        validateSortField(filter);
    }

    private void validateId(TrainingFilter filter, List<String> validationErrors) {
        if (filter.getId() != null && filter.getId() < 1) {
            validationErrors.add("ID must be a positive number");
        }
    }

    private void validateTrainingAreaId(TrainingFilter filter, List<String> validationErrors) {
        if (filter.getTrainingAreaId() != null && filter.getTrainingAreaId() < 1) {
            validationErrors.add("Training area ID must be a positive number");
        }
    }

    private void validateLanguage(TrainingFilter filter, List<String> validationErrors) {
        TrainingLanguage language = filter.getLanguage();

        // No need to validate language if it's null as it's an optional filter
        if (language == null) {
            return;
        }
        
        // The language is already guaranteed to be a valid enum value if it's not null
    }

    private void validateDuration(TrainingFilter filter, List<String> validationErrors) {
        Integer duration = filter.getDuration();
        if (duration != null) {
            if (duration < MIN_DURATION) {
                validationErrors.add("Duration must be a positive number");
            }

            if (duration > MAX_DURATION) {
                validationErrors.add("Duration cannot exceed 1000");
            }
        }
    }

    private void validateStartDate(TrainingFilter filter, List<String> validationErrors) {
        String startDate = filter.getStartDate();
        if (startDate != null && !startDate.isEmpty()) {
            try {
                Instant.parse(startDate); // expects format like 2025-07-01T09:00:00Z
            } catch (DateTimeParseException e) {
                validationErrors.add("Start date must be in ISO-8601 format, e.g., 2025-07-01T09:00:00Z");
            }
        }
    }

    private void validateSearchText(TrainingFilter filter, List<String> validationErrors) {
        String searchText = filter.getSearchText();
        if (searchText != null && searchText.length() > MAX_SEARCH_TEXT_LENGTH) {
            validationErrors.add("Search text cannot exceed 255 characters");
        }
    }

    private void validateSortField(TrainingFilter filter) {
        if (filter.getSortField() == null) {
            filter.setSortField(TrainingSortField.DATE); // Set default if null
        }
    }
}
