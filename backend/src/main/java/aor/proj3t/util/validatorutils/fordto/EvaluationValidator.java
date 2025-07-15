package aor.proj3t.util.validatorutils.fordto;

import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.exceptions.InvalidDataException;
import aor.proj3t.filters.DateRange;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;

/**
 * Validator class for evaluation-related DTOs and domain constraints.
 * <p>
 * Provides centralized validation logic for evaluation entities,
 * such as ID checks, feedback length enforcement, scale and status validation,
 * and constraints for evaluation cycle ranges. Ensures correctness
 * before data proceeds to service and persistence layers.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class EvaluationValidator {

    /**
     * Default constructor.
     * Required for dependency injection and scoped lifecycle.
     */
    public EvaluationValidator() {
        // Default constructor
    }

    /**
     * Validates that an ID value is a positive number.
     *
     * @param id the ID to validate
     * @param fieldName the name of the field being validated
     * @throws InvalidDataException if ID is null or not positive
     */
    public void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(fieldName + " ID must be a positive number");
        }
    }

    /**
     * Validates the content length of feedback.
     *
     * @param feedback the feedback string to validate
     * @throws InvalidDataException if feedback is too short or too long
     */
    public void validateFeedback(String feedback) {
        if (feedback != null && (feedback.length() < 3 || feedback.length() > 1000)) {
            throw new InvalidDataException("Feedback must be between 3 and 1000 characters if provided");
        }
    }

    /**
     * Validates that the given evaluation scale is recognized.
     *
     * @param scale the scale to validate
     * @throws InvalidDataException if scale is null or not valid
     */
    public void validateScale(EvaluationScale scale) {
        if (scale == null) {
            throw new InvalidDataException("Evaluation scale must be provided");
        }
        boolean matched = false;
        for (EvaluationScale scaleValue : EvaluationScale.values()) {
            if (scaleValue.name().equalsIgnoreCase(scale.name())) {
                matched = true;
                break;
            }
        }
        if (!matched) {
            throw new InvalidDataException("Invalid evaluation scale: " + scale);
        }
    }

    /**
     * Validates that the given evaluation status is recognized.
     *
     * @param status the status to validate
     * @throws InvalidDataException if status is null or not valid
     */
    public void validateState(EvaluationStatus status) {
        if (status == null) {
            throw new InvalidDataException("Evaluation state must be provided");
        }
        boolean matched = false;
        for (EvaluationStatus evalStatus : EvaluationStatus.values()) {
            if (evalStatus.name().equalsIgnoreCase(status.name())) {
                matched = true;
                break;
            }
        }
        if (!matched) {
            throw new InvalidDataException("Invalid evaluation status: " + status);
        }
    }

    /**
     * Validates a complete {@link EvaluationDTO} structure.
     *
     * @param dto the evaluation DTO to validate
     * @throws InvalidDataException if any field fails validation
     */
    public void validateEvaluationDTO(EvaluationDTO dto) {
        validateId(dto.getEvaluationCycle().getId(), "Evaluation Cycle");
        validateId(dto.getEvaluatee().getId(), "Evaluatee");
        validateId(dto.getEvaluator().getId(), "Evaluator");
        validateScale(dto.getScale());
        validateState(dto.getStatus());
        validateFeedback(dto.getFeedback());
    }

    /**
     * Validates that a list of evaluation IDs is present and non-empty.
     *
     * @param evaluationIdList the list to validate
     * @throws InvalidDataException if list is null or empty
     */
    public void validateEvaluationIdList(List<Long> evaluationIdList) {
        if (evaluationIdList == null || evaluationIdList.isEmpty()) {
            throw new InvalidDataException("Evaluation list must not be empty");
        }
    }

    /**
     * Validates a {@link DateRange} object for starting a new evaluation cycle.
     * Ensures non-null dates, proper chronological order, and that start date is today.
     *
     * @param dateRange the date range to validate
     * @throws InvalidDataException if any constraint is violated
     */
    public void newCycleDateRangeValidation(DateRange dateRange) throws InvalidDataException {
        if (dateRange == null) {
            throw new InvalidDataException("Date range must not be null");
        }
        if (dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
            throw new InvalidDataException(
                    "start date and end date are required",
                    "Evaluation cycle started without start and end dates");
        }
        if (dateRange.getStartDate().isAfter(dateRange.getEndDate())) {
            throw new InvalidDataException(
                    "start date must be before end date",
                    "Evaluation cycle started without start end after end date");
        }

        Instant now = Instant.now();
        java.time.LocalDate startDate = dateRange.getStartDate()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        java.time.LocalDate today = now
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        if (!startDate.equals(today)) {
            throw new InvalidDataException(
                    "start date must be on the current day",
                    "Evaluation cycle must start on the current day");
        }
    }

    /**
     * Validates a {@link EvaluationCloseDTO} structure for bulk closure operations.
     *
     * @param dto the DTO containing cycle ID and evaluation ID list
     * @throws InvalidDataException if data is missing or invalid
     */
    public void validateEvaluationCloseDTO(EvaluationCloseDTO dto) {
        validateId(dto.getCycleId(), "Evaluation Cycle");
        validateEvaluationIdList(dto.getEvaluationIds());
    }
}
