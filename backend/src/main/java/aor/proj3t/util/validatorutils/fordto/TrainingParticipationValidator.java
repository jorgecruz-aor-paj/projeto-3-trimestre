package aor.proj3t.util.validatorutils.fordto;

import aor.proj3t.dto.NewTrainingParticipationDTO;
import aor.proj3t.exceptions.InvalidDataException;

import java.time.Instant;

public class TrainingParticipationValidator {

    public TrainingParticipationValidator() {}


    public void validateId(Long id, String entity) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(entity + " ID must be a positive number");
        }
    }

    public void validateParticipationDate(Instant participationDate) {
            if(participationDate.isAfter(Instant.now())){
                throw new InvalidDataException(
                        "Date must be after now",
                        "Training participation requested for a future date");
            }
    }

    public void validateNewTrainingParticipation(NewTrainingParticipationDTO newTrainingParticipationDto) {
        validateId(newTrainingParticipationDto.getTrainingId(), "Training");
        validateId(newTrainingParticipationDto.getManagerId(), "User");
        validateId(newTrainingParticipationDto.getParticipantId(), "User");
        validateParticipationDate(newTrainingParticipationDto.getParticipatedAt());
    }


}
