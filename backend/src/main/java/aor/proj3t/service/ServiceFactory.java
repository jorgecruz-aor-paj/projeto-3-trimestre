package aor.proj3t.service;

import aor.proj3t.service.interfaces.AIService;
import aor.proj3t.service.interfaces.AsyncEmailService;
import aor.proj3t.service.interfaces.AuthService;
import aor.proj3t.service.interfaces.AuthorizationService;
import aor.proj3t.service.interfaces.EvaluationCycleService;
import aor.proj3t.service.interfaces.EvaluationService;
import aor.proj3t.service.interfaces.ExportService;
import aor.proj3t.service.interfaces.MessageService;
import aor.proj3t.service.interfaces.NotificationService;
import aor.proj3t.service.interfaces.StatsService;
import aor.proj3t.service.interfaces.TokenService;
import aor.proj3t.service.interfaces.TrainingParticipationService;
import aor.proj3t.service.interfaces.TrainingService;
import aor.proj3t.service.interfaces.TwoFactorAuthService;
import aor.proj3t.service.interfaces.UserService;
import aor.proj3t.service.interfaces.WorkplaceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Factory class to access all service implementations. This class provides a
 * centralized access
 * point for all the services in the application. It follows the Factory design
 * pattern to
 * encapsulate the instantiation logic of services and provides a clean,
 * consistent API for
 * accessing them. The factory is injected with all available services and
 * offers type-safe methods
 * to retrieve them.
 *
 * <p>
 * Example usage:
 *
 * <pre>
 * User user = serviceFactory.forUser().findById(userId);
 * List<Training> trainings = serviceFactory.forTraining().getAllTrainings();
 * </pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 *
 * @version 1.0
 */
@ApplicationScoped
public class ServiceFactory {

    @Inject
    private UserService userService;

    @Inject
    private AuthService authService;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private TrainingService trainingService;

    @Inject
    private TrainingParticipationService trainingParticipationService;

    @Inject
    private EvaluationService evaluationService;

    @Inject
    private EvaluationCycleService evaluationCycleService;

    @Inject
    private MessageService messageService;

    @Inject
    private NotificationService notificationService;

    @Inject
    private AIService aiService;

    @Inject
    private ExportService exportService;

    @Inject
    private WorkplaceService workplaceService;

    @Inject
    private TokenService tokenService;

    @Inject
    private AsyncEmailService asyncEmailService;

    @Inject
    private StatsService statsService;

    @Inject
    private TwoFactorAuthService twoFactorAuthService;

    /**
     * Get the service for User operations This method provides access to the
     * service responsible
     * for user-related business logic and operations. The returned service handles
     * all user
     * management functionality such as creating, updating, retrieving, and
     * authenticating users.
     *
     * @return The UserService instance
     */
    public UserService forUser() {
        return userService;
    }

    /**
     * Get the service for Authentication operations
     *
     * @return The AuthService instance
     */
    public AuthService forAuth() {
        return authService;
    }

    /**
     * Get the service for Training operations
     *
     * @return The TrainingService instance
     */
    public TrainingService forTraining() {
        return trainingService;
    }

    /**
     * Get the service for TrainingParticipation operations
     *
     * @return The TrainingParticipationService instance
     */
    public TrainingParticipationService forTrainingParticipation() {
        return trainingParticipationService;
    }

    /**
     * Get the service for Evaluation operations
     *
     * @return The EvaluationService instance
     */
    public EvaluationService forEvaluation() {
        return evaluationService;
    }

    /**
     * Get the service for EvaluationCycle operations
     *
     * @return The EvaluationCycleService instance
     */
    public EvaluationCycleService forEvaluationCycle() {
        return evaluationCycleService;
    }

    /**
     * Get the service for Token operations *
     * 
     * @return The TokenService instance
     */
    public TokenService forToken() {
        return tokenService;
    }

    /**
     * Get the service for Message operations
     * 
     * @return The MessageService instance
     */
    public MessageService forMessage() {
        return messageService;
    }

    /**
     * Get the service for Notification operations
     * 
     * @return The NotificationService instance
     */
    public NotificationService forNotification() {
        return notificationService;
    }

    /**
     * Get the service for Authorization operations
     * 
     * @return The AuthorizationService instance
     */
    public AuthorizationService forAuthorization() {
        return authorizationService;
    }

    /**
     * Get the service for Export operations
     *
     * @return The ExportService instance
     */
    public ExportService forExport() {
        return exportService;
    }

    /**
     * Get the service for AI operations
     * This method provides access to the service responsible for AI-related
     * business logic and operations, such as sentiment analysis, training
     * recommendations,
     * and performance predictions.
     *
     * @return The AIService instance
     */
    public AIService forAI() {
        return aiService;
    }

    /**
     * Get the service for Workplace operations
     *
     * @return The WorkplaceService instance
     */
    public WorkplaceService forWorkplace() {
        return workplaceService;
    }

    /**
     * Get the service for Asynchronous Email operations
     *
     * @return The AsyncEmailService instance
     */
    public AsyncEmailService forAsyncEmail() {
        return asyncEmailService;
    }

    /**
     * Get the service for Statistics operations
     * This method provides access to the service responsible for
     * statistics-related business logic including personal user statistics,
     * performance trends, and training analytics.
     *
     * @return The StatsService instance
     */
    public StatsService forStats() {
        return statsService;
    }

    /**
     * Get the service for Two-Factor Authentication operations
     * This method provides access to the service responsible for
     * 2FA-related business logic including TOTP setup, verification,
     * backup codes management, and 2FA security operations.
     *
     * @return The TwoFactorAuthService instance
     */
    public TwoFactorAuthService forTwoFactorAuth() {
        return twoFactorAuthService;
    }

}
