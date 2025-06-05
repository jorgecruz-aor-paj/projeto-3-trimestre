package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.MessageDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Factory class to access all entity-DTO converters
 */
@ApplicationScoped
public class ConverterFactory {

    @Inject
    private UserConverter userConverter;

    @Inject
    private MessageConverter messageConverter;

    @Inject
    private TrainingParticipationConverter trainingParticipationConverter;

    /**
     * Get the converter for User entities and DTOs
     * 
     * @return The UserConverter instance
     */
    public Converter<User, UserDTO> forUser() {
        return userConverter;
    }

    /**
     * Get the converter for Message entities and DTOs
     * 
     * @return The MessageConverter instance
     */
    public Converter<Message, MessageDTO> forMessage() {
        return messageConverter;
    }

    /**
     * Get the converter for TrainingParticipation entities and DTOs
     * 
     * @return The TrainingParticipationConverter instance
     */
    public Converter<TrainingParticipation, TrainingParticipationDTO> forTrainingParticipation() {
        return trainingParticipationConverter;
    }
}
