package aor.proj3t.service;

import aor.proj3t.service.impl.TokenService;
import aor.proj3t.service.interfaces.AuthService;
import aor.proj3t.service.interfaces.EvaluationCycleService;
import aor.proj3t.service.interfaces.EvaluationService;
import aor.proj3t.service.interfaces.TokenRevocationService;
import aor.proj3t.service.interfaces.TrainingParticipationService;
import aor.proj3t.service.interfaces.TrainingService;
import aor.proj3t.service.interfaces.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Factory class to access all service implementations.
 * This class provides a centralized access point for all the services in the
 * application.
 * It follows the Factory design pattern to encapsulate the instantiation logic
 * of services
 * and provides a clean, consistent API for accessing them. The factory is
 * injected with all
 * available services and offers type-safe methods to retrieve them.
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
    private TrainingService trainingService;

    @Inject
    private TrainingParticipationService trainingParticipationService;

    @Inject
    private EvaluationService evaluationService;

    @Inject
    private EvaluationCycleService evaluationCycleService;

    @Inject
    private TokenService tokenService;

    @Inject
    private TokenRevocationService tokenRevocationService;

    /**
     * Get the service for User operations
     * This method provides access to the service responsible for user-related
     * business logic and operations. The returned service handles all user
     * management
     * functionality such as creating, updating, retrieving, and authenticating
     * users.
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
     * Get the service for Token operations
     *
     * @return The TokenService instance
     */
    public TokenService forToken() {
        return tokenService;
    }

    /**
     * Get the service for TokenRevocation operations
     *
     * @return The TokenRevocationService instance
     */
    public TokenRevocationService forTokenRevocation() {
        return tokenRevocationService;
    }
}
