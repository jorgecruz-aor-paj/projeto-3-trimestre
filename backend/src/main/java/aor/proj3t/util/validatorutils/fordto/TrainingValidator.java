package aor.proj3t.util.validatorutils.fordto;

import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.exceptions.InvalidDataException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.regex.Pattern;

@ApplicationScoped
public class TrainingValidator {

    public TrainingValidator() {
        // Default constructor
    }

    public void validateId(Long id, String entity) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(entity + " ID must be a positive number");
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





    public void validateNewTrainingDTO(NewTrainingDTO newTrainingDto) {
        validateTrainingTitle(newTrainingDto.getTitle());
        validateTrainingDuration(newTrainingDto.getDuration());
        validateTrainingDescription(newTrainingDto.getDescription());
        validateTrainingLink(newTrainingDto.getLink());
        validateTrainingLanguage(newTrainingDto.getLanguage());
        validateId(newTrainingDto.getTrainingAreaId(), "Training Area");
    }

    public void validateTrainingDTO(TrainingDTO trainingDto) {
        validateId(trainingDto.getId(), "Training");
        validateTrainingTitle(trainingDto.getTitle());
        validateTrainingDuration(trainingDto.getDuration());
        validateTrainingDescription(trainingDto.getDescription());
        validateTrainingLink(trainingDto.getLink());
        validateTrainingLanguage(trainingDto.getLanguage());
        validateId(trainingDto.getTrainingArea().getId(), "Training Area");
    }

    public void validateTrainingAreaDTO(TrainingAreaDTO trainingAreaDto) {
        validateTrainingTitle(trainingAreaDto.getName());
    }


}
