package aor.proj3t.util.validatorutils.forfilters;

import aor.proj3t.exceptions.InvalidDataException;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class for validating filter objects.
 * <p>
 * Provides a common validation framework for filters using Jakarta Bean Validation
 * and allows subclasses to implement additional custom validation logic via {@link #doValidate}.
 * This approach enforces standardized validation behavior across all filter types.
 * </p>
 *
 * @param <T> the type of filter to validate
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseFilterValidator<T> {

    @Inject
    protected Validator validator;

    /**
     * Validates a filter object using Jakarta Bean Validation and subclass-defined rules.
     * Throws an {@link InvalidDataException} if any violations are detected.
     *
     * @param filter the filter object to validate
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
     * Subclasses must implement this method to define custom validations
     * for their specific filter types. Append validation messages to the list
     * if any issues are found.
     *
     * @param filter the filter to validate
     * @param validationErrors list for collecting validation error messages
     */
    protected abstract void doValidate(T filter, List<String> validationErrors);
}

