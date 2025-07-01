package aor.proj3t.util.validatorutils.fordto;

import aor.proj3t.dto.*;
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
        validateId(dto.getEvaluationCycleId(), "Evaluation Cycle");
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
        if(dateRange.getStartDate().isBefore(Instant.now())) {
            throw new InvalidDataException(
                    "start date should be after current time",
                    "Evaluation cycle started without start date previous to current time");
        }
    }

    public void validateEvaluationCloseDTO(EvaluationCloseDTO dto) {
        validateId(dto.getCycleId(), "Evaluation Cycle");
        validateEvaluationIdList(dto.getEvaluationIds());
    }
}