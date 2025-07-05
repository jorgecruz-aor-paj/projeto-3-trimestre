package aor.proj3t.util.validatorutils.forfilters;

import aor.proj3t.exceptions.InvalidDataException;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Base validator class that provides common validation functionality
 * for all filter validators.
 * 
 * @param <T> The type of filter to validate
 */
public abstract class BaseFilterValidator<T> {

    @Inject
    protected Validator validator;

    /**
     * Validates a filter object using Jakarta Bean Validation and custom
     * validations
     * 
     * @param filter The filter to validate
     */
    public void validate(T filter) {
        // Jakarta Bean Validation
        Set<ConstraintViolation<T>> violations = validator.validate(filter);
        if (!violations.isEmpty()) {
            throw new InvalidDataException(
                    "Invalid filter data",
                    "Validation error");
        }

        List<String> validationErrors = new ArrayList<>();

        // Perform custom validations
        doValidate(filter, validationErrors);

        // If there are validation errors, throw exception with all errors
        if (!validationErrors.isEmpty()) {
            throw new InvalidDataException(
                    "Invalid filter data",
                    String.join("; ", validationErrors));
        }
    }

    /**
     * Implement this method in subclasses to add specific validations for each
     * filter type
     * 
     * @param filter           The filter to validate
     * @param validationErrors List to collect validation error messages
     */
    protected abstract void doValidate(T filter, List<String> validationErrors);
}
