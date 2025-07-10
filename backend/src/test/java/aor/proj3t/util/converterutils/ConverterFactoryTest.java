package aor.proj3t.util.converterutils;

import aor.proj3t.dto.*;
import aor.proj3t.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConverterFactory Tests")
public class ConverterFactoryTest {

    @Mock
    private UserConverter userConverter;

    @Mock
    private MessageConverter messageConverter;

    @Mock
    private TrainingParticipationConverter trainingParticipationConverter;

    @Mock
    private WorkplaceConverter workplaceConverter;

    @Mock
    private NotificationConverter notificationConverter;

    @Mock
    private EvaluationConverter evaluationConverter;

    @Mock
    private SessionConverter sessionConverter;

    @Mock
    private TrainingConverter trainingConverter;

    @Mock
    private TrainingAreaConverter trainingAreaConverter;

    @Mock
    private EvaluationCycleConverter evaluationCycleConverter;

    @Mock
    private ResetPasswordConverter resetPasswordConverter;

    @Mock
    private PendingRegistrationConverter pendingRegistrationConverter;

    @Mock
    private SettingsConverter settingsConverter;

    @InjectMocks
    private ConverterFactory converterFactory;

    @Test
    @DisplayName("Should return UserConverter")
    public void testForUser() {
        // When
        Converter<User, UserDTO> result = converterFactory.forUser();
        
        // Then
        assertNotNull(result);
        assertEquals(userConverter, result);
    }

    @Test
    @DisplayName("Should return MessageConverter")
    public void testForMessage() {
        // When
        Converter<Message, MessageDTO> result = converterFactory.forMessage();
        
        // Then
        assertNotNull(result);
        assertEquals(messageConverter, result);
    }

    @Test
    @DisplayName("Should return TrainingParticipationConverter")
    public void testForTrainingParticipation() {
        // When
        Converter<TrainingParticipation, TrainingParticipationDTO> result = converterFactory.forTrainingParticipation();
        
        // Then
        assertNotNull(result);
        assertEquals(trainingParticipationConverter, result);
    }

    @Test
    @DisplayName("Should return WorkplaceConverter")
    public void testForWorkplace() {
        // When
        Converter<Workplace, WorkplaceDTO> result = converterFactory.forWorkplace();
        
        // Then
        assertNotNull(result);
        assertEquals(workplaceConverter, result);
    }

    @Test
    @DisplayName("Should return NotificationConverter")
    public void testForNotification() {
        // When
        Converter<Notification, NotificationDTO> result = converterFactory.forNotification();
        
        // Then
        assertNotNull(result);
        assertEquals(notificationConverter, result);
    }

    @Test
    @DisplayName("Should return EvaluationConverter")
    public void testForEvaluation() {
        // When
        Converter<Evaluation, EvaluationDTO> result = converterFactory.forEvaluation();
        
        // Then
        assertNotNull(result);
        assertEquals(evaluationConverter, result);
    }

    @Test
    @DisplayName("Should return SessionConverter")
    public void testForSession() {
        // When
        Converter<Session, SessionDTO> result = converterFactory.forSession();
        
        // Then
        assertNotNull(result);
        assertEquals(sessionConverter, result);
    }

    @Test
    @DisplayName("Should return TrainingConverter")
    public void testForTraining() {
        // When
        Converter<Training, TrainingDTO> result = converterFactory.forTraining();
        
        // Then
        assertNotNull(result);
        assertEquals(trainingConverter, result);
    }

    @Test
    @DisplayName("Should return TrainingAreaConverter")
    public void testForTrainingArea() {
        // When
        Converter<TrainingArea, TrainingAreaDTO> result = converterFactory.forTrainingArea();
        
        // Then
        assertNotNull(result);
        assertEquals(trainingAreaConverter, result);
    }

    @Test
    @DisplayName("Should return EvaluationCycleConverter")
    public void testForEvaluationCycle() {
        // When
        Converter<EvaluationCycle, EvaluationCycleDTO> result = converterFactory.forEvaluationCycle();
        
        // Then
        assertNotNull(result);
        assertEquals(evaluationCycleConverter, result);
    }

    @Test
    @DisplayName("Should return ResetPasswordConverter")
    public void testForResetPassword() {
        // When
        Converter<ResetPassword, ResetPasswordDTO> result = converterFactory.forResetPassword();
        
        // Then
        assertNotNull(result);
        assertEquals(resetPasswordConverter, result);
    }

    @Test
    @DisplayName("Should return PendingRegistrationConverter")
    public void testForPendingRegistration() {
        // When
        Converter<PendingRegistration, PendingRegistrationDTO> result = converterFactory.forPendingRegistration();
        
        // Then
        assertNotNull(result);
        assertEquals(pendingRegistrationConverter, result);
    }

    @Test
    @DisplayName("Should return SettingsConverter")
    public void testForSettings() {
        // When
        Converter<Settings, SettingsDTO> result = converterFactory.forSettings();
        
        // Then
        assertNotNull(result);
        assertEquals(settingsConverter, result);
    }
}
