package aor.proj3t.util.validatorutils.fordto;

import java.util.regex.Pattern;

import aor.proj3t.config.AppConstants;
import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.enums.TrainingLanguage;
import aor.proj3t.exceptions.InvalidDataException;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Validator class for training-related DTOs and attributes.
 * <p>
 * Performs validation checks for training creation, updates, and metadata such as titles,
 * durations, descriptions, URLs, and language enums. Centralizes input validation logic
 * to prevent invalid data from reaching service or persistence layers.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class TrainingValidator {

    /**
     * Default constructor.
     * Necessary for instantiation in DI contexts.
     */
    public TrainingValidator() {
        // Default constructor
    }

    /**
     * Validates that an ID is non-null and greater than zero.
     *
     * @param id the ID value to validate
     * @param entity the entity name for error messaging context
     * @throws InvalidDataException if ID is null or non-positive
     */
    public void validateId(Long id, String entity) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(entity + " ID must be a positive number");
        }
    }

    /**
     * Validates that the training title is present and under character limits.
     *
     * @param title the title string to validate
     * @throws InvalidDataException if title is null, empty, or too long
     */
    public void validateTrainingTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException("Title is required");
        }
        if (title.length() > 100) {
            throw new InvalidDataException("Title must not exceed 100 characters");
        }
    }

    /**
     * Validates that the training duration is a positive number.
     *
     * @param duration the duration in minutes
     * @throws InvalidDataException if duration is null or non-positive
     */
    public void validateTrainingDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new InvalidDataException("Duration must be a positive number of minutes");
        }
    }

    /**
     * Validates that the training description is within acceptable character bounds.
     *
     * @param description the description text
     * @throws InvalidDataException if description is too short or too long
     */
    public void validateTrainingDescription(String description) {
        if (description == null || description.length() < 10 || description.length() > 1000) {
            throw new InvalidDataException("Description must be between 10 and 1000 characters");
        }
    }

    /**
     * Validates that a training link, if provided, matches the expected URL format.
     *
     * @param link the link to validate (optional)
     * @throws InvalidDataException if link is too long or not a valid URL
     */
    public void validateTrainingLink(String link) {
        if (link != null && !link.isEmpty()) {
            if (link.length() > AppConstants.Validation.MAX_URL_LENGTH) {
                throw new InvalidDataException(
                        "Link must not exceed " + AppConstants.Validation.MAX_URL_LENGTH + " characters");
            }
            if (!Pattern.matches(AppConstants.Validation.URL_REGEX, link)) {
                throw new InvalidDataException("Link is not a valid URL format");
            }
        }
    }

    /**
     * Validates that a training language is specified.
     *
     * @param language the training language enum
     * @throws InvalidDataException if language is null
     */
    public void validateTrainingLanguage(TrainingLanguage language) {
        if (language == null) {
            throw new InvalidDataException("Training language must be provided");
        }
    }

    /**
     * Validates a {@link NewTrainingDTO} for training creation.
     *
     * @param newTrainingDto the DTO containing training details
     * @throws InvalidDataException if any field is invalid
     */
    public void validateNewTrainingDTO(NewTrainingDTO newTrainingDto) {
        validateTrainingTitle(newTrainingDto.getTitle());
        validateTrainingDuration(newTrainingDto.getDuration());
        validateTrainingDescription(newTrainingDto.getDescription());
        validateTrainingLink(newTrainingDto.getLink());
        validateTrainingLanguage(newTrainingDto.getLanguage());
        validateId(newTrainingDto.getTrainingAreaId(), "Training Area");
    }

    /**
     * Validates a {@link TrainingDTO} for training update or persistence.
     *
     * @param trainingDto the DTO containing training information
     * @throws InvalidDataException if any field is invalid
     */
    public void validateTrainingDTO(TrainingDTO trainingDto) {
        validateId(trainingDto.getId(), "Training");
        validateTrainingTitle(trainingDto.getTitle());
        validateTrainingDuration(trainingDto.getDuration());
        validateTrainingDescription(trainingDto.getDescription());
        validateTrainingLink(trainingDto.getLink());
        validateTrainingLanguage(trainingDto.getLanguage());
        validateId(trainingDto.getTrainingArea().getId(), "Training Area");
    }

    /**
     * Validates a {@link TrainingAreaDTO}, primarily checking its name.
     *
     * @param trainingAreaDto the DTO representing a training area
     * @throws InvalidDataException if the name is invalid
     */
    public void validateTrainingAreaDTO(TrainingAreaDTO trainingAreaDto) {
        validateTrainingTitle(trainingAreaDto.getName());
    }
}

