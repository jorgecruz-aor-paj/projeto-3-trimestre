package aor.proj3t.util.validatorutils.fordto;

import aor.proj3t.dto.NewTrainingParticipationDTO;
import aor.proj3t.exceptions.InvalidDataException;

import java.time.Instant;
/**
 * Validator class for training participation operations.
 * <p>
 * Provides centralized validation logic for ensuring data correctness when
 * recording training participations. Validates IDs, date constraints,
 * and structural completeness of participation DTOs before they are persisted or processed.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class TrainingParticipationValidator {

    /**
     * Default constructor.
     * Required for instantiation and DI in application scope.
     */
    public TrainingParticipationValidator() {}

    /**
     * Validates that the provided ID is positive and non-null.
     *
     * @param id the ID to validate
     * @param entity the name of the entity being validated (for error message context)
     * @throws InvalidDataException if ID is null or non-positive
     */
    public void validateId(Long id, String entity) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(entity + " ID must be a positive number");
        }
    }

    /**
     * Validates that the participation date is not in the future.
     *
     * @param participationDate the timestamp of participation
     * @throws InvalidDataException if the date is in the future
     */
    public void validateParticipationDate(Instant participationDate) {
        if (participationDate.isAfter(Instant.now())) {
            throw new InvalidDataException(
                    "Date must be after now",
                    "Training participation requested for a future date");
        }
    }

    /**
     * Validates a {@link NewTrainingParticipationDTO} object by checking IDs and date.
     *
     * @param newTrainingParticipationDto the DTO to validate
     * @throws InvalidDataException if any required field is invalid
     */
    public void validateNewTrainingParticipation(NewTrainingParticipationDTO newTrainingParticipationDto) {
        validateId(newTrainingParticipationDto.getTrainingId(), "Training");
        validateId(newTrainingParticipationDto.getManagerId(), "User");
        validateId(newTrainingParticipationDto.getParticipantId(), "User");
        validateParticipationDate(newTrainingParticipationDto.getParticipatedAt());
    }
}

