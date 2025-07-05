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

@ApplicationScoped
public class EvaluationValidator {


    public EvaluationValidator() {
        // Default constructor
    }

    public void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(fieldName + " ID must be a positive number");
        }
    }

    public void validateFeedback(String feedback) {
        if (feedback != null && (feedback.length() < 3 || feedback.length() > 1000)) {
            throw new InvalidDataException("Feedback must be between 3 and 1000 characters if provided");
        }
    }

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

    public void validateEvaluationDTO(EvaluationDTO dto) {
        validateId(dto.getEvaluationCycle().getId(), "Evaluation Cycle");
        validateId(dto.getEvaluatee().getId(), "Evaluatee");
        validateId(dto.getEvaluator().getId(), "Evaluator");
        validateScale(dto.getScale());
        validateState(dto.getStatus());
        validateFeedback(dto.getFeedback());
    }

    public void validateEvaluationIdList(List<Long> evaluationIdList) {
        if (evaluationIdList == null || evaluationIdList.isEmpty()) {
            throw new InvalidDataException("Evaluation list must not be empty");
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
        // Validate that start date is on the current day (not before or after)
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

    public void validateEvaluationCloseDTO(EvaluationCloseDTO dto) {
        validateId(dto.getCycleId(), "Evaluation Cycle");
        validateEvaluationIdList(dto.getEvaluationIds());
    }
}