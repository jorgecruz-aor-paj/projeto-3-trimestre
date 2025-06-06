package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.dto.MessageDTO;
import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.dto.PendingRegistrationDTO;
import aor.proj3t.dto.ResetPasswordDTO;
import aor.proj3t.dto.SessionDTO;
import aor.proj3t.dto.SettingsDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.ResetPassword;
import aor.proj3t.entities.Session;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
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

    @Inject
    private WorkplaceConverter workplaceConverter;

    @Inject
    private NotificationConverter notificationConverter;

    @Inject
    private EvaluationConverter evaluationConverter;

    @Inject
    private EvaluationCycleConverter evaluationCycleConverter;

    @Inject
    private SettingsConverter settingsConverter;

    @Inject
    private ResetPasswordConverter resetPasswordConverter;

    @Inject
    private PendingRegistrationConverter pendingRegistrationConverter;

    @Inject
    private SessionConverter sessionConverter;

    @Inject
    private TrainingConverter trainingConverter;

    @Inject
    private TrainingAreaConverter trainingAreaConverter;

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

    /**
     * Get the converter for Workplace entities and DTOs
     * 
     * @return The WorkplaceConverter instance
     */
    public Converter<Workplace, WorkplaceDTO> forWorkplace() {
        return workplaceConverter;
    }

    /**
     * Get the converter for Notification entities and DTOs
     * 
     * @return The NotificationConverter instance
     */
    public Converter<Notification, NotificationDTO> forNotification() {
        return notificationConverter;
    }

    /**
     * Get the converter for Evaluation entities and DTOs
     * 
     * @return The EvaluationConverter instance
     */
    public Converter<Evaluation, EvaluationDTO> forEvaluation() {
        return evaluationConverter;
    }

    /**
     * Get the converter for EvaluationCycle entities and DTOs
     * 
     * @return The EvaluationCycleConverter instance
     */
    public Converter<EvaluationCycle, EvaluationCycleDTO> forEvaluationCycle() {
        return evaluationCycleConverter;
    }

    /**
     * Get the converter for Settings entities and DTOs
     * 
     * @return The SettingsConverter instance
     */
    public Converter<Settings, SettingsDTO> forSettings() {
        return settingsConverter;
    }

    /**
     * Get the converter for ResetPassword entities and DTOs
     * 
     * @return The ResetPasswordConverter instance
     */
    public Converter<ResetPassword, ResetPasswordDTO> forResetPassword() {
        return resetPasswordConverter;
    }

    /**
     * Get the converter for PendingRegistration entities and DTOs
     * 
     * @return The PendingRegistrationConverter instance
     */
    public Converter<PendingRegistration, PendingRegistrationDTO> forPendingRegistration() {
        return pendingRegistrationConverter;
    }

    /**
     * Get the converter for Session entities and DTOs
     * 
     * @return The SessionConverter instance
     */
    public Converter<Session, SessionDTO> forSession() {
        return sessionConverter;
    }

    /**
     * Get the converter for Training entities and DTOs
     * 
     * @return The TrainingConverter instance
     */
    public Converter<Training, TrainingDTO> forTraining() {
        return trainingConverter;
    }

    /**
     * Get the converter for TrainingArea entities and DTOs
     * 
     * @return The TrainingAreaConverter instance
     */
    public Converter<TrainingArea, TrainingAreaDTO> forTrainingArea() {
        return trainingAreaConverter;
    }
}
