package aor.proj3t.daos;

import aor.proj3t.daos.interfaces.EvaluationCycleDAO;
import aor.proj3t.daos.interfaces.EvaluationDAO;
import aor.proj3t.daos.interfaces.MessageDAO;
import aor.proj3t.daos.interfaces.NotificationDAO;
import aor.proj3t.daos.interfaces.PendingRegistrationDAO;
import aor.proj3t.daos.interfaces.ResetPasswordDAO;
import aor.proj3t.daos.interfaces.SessionDAO;
import aor.proj3t.daos.interfaces.SettingsDAO;
import aor.proj3t.daos.interfaces.TrainingAreaDAO;
import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.daos.interfaces.TrainingParticipationDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.daos.interfaces.WorkplaceDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Factory class to access all DAO implementations.
 * This class provides a centralized access point for all the DAOs in the
 * application.
 * It follows the Factory design pattern to encapsulate the instantiation logic
 * of DAOs
 * and provides a clean, consistent API for accessing them.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * User user = daoFactory.forUser().findById(userId).orElse(null);
 * List<Training> trainings = daoFactory.forTraining().findAll();
 * </pre>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class DAOFactory {

    @Inject
    private UserDAO userDAO;

    @Inject
    private TrainingDAO trainingDAO;

    @Inject
    private TrainingAreaDAO trainingAreaDAO;

    @Inject
    private TrainingParticipationDAO trainingParticipationDAO;

    @Inject
    private MessageDAO messageDAO;

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    private EvaluationDAO evaluationDAO;

    @Inject
    private EvaluationCycleDAO evaluationCycleDAO;

    @Inject
    private SettingsDAO settingsDAO;

    @Inject
    private SessionDAO sessionDAO;

    @Inject
    private ResetPasswordDAO resetPasswordDAO;
    @Inject
    private PendingRegistrationDAO pendingRegistrationDAO;

    @Inject
    private WorkplaceDAO workplaceDAO;

    /**
     * Get the DAO for User entities
     * 
     * @return The UserDAO instance
     */
    public UserDAO forUser() {
        return userDAO;
    }

    /**
     * Get the DAO for Training entities
     * 
     * @return The TrainingDAO instance
     */
    public TrainingDAO forTraining() {
        return trainingDAO;
    }

    /**
     * Get the DAO for TrainingArea entities
     * 
     * @return The TrainingAreaDAO instance
     */
    public TrainingAreaDAO forTrainingArea() {
        return trainingAreaDAO;
    }

    /**
     * Get the DAO for TrainingParticipation entities
     * 
     * @return The TrainingParticipationDAO instance
     */
    public TrainingParticipationDAO forTrainingParticipation() {
        return trainingParticipationDAO;
    }

    /**
     * Get the DAO for Message entities
     * 
     * @return The MessageDAO instance
     */
    public MessageDAO forMessage() {
        return messageDAO;
    }

    /**
     * Get the DAO for Notification entities
     * 
     * @return The NotificationDAO instance
     */
    public NotificationDAO forNotification() {
        return notificationDAO;
    }

    /**
     * Get the DAO for Evaluation entities
     * 
     * @return The EvaluationDAO instance
     */
    public EvaluationDAO forEvaluation() {
        return evaluationDAO;
    }

    /**
     * Get the DAO for EvaluationCycle entities
     * 
     * @return The EvaluationCycleDAO instance
     */
    public EvaluationCycleDAO forEvaluationCycle() {
        return evaluationCycleDAO;
    }

    /**
     * Get the DAO for Settings entities
     * 
     * @return The SettingsDAO instance
     */
    public SettingsDAO forSettings() {
        return settingsDAO;
    }

    /**
     * Get the DAO for Session entities
     * 
     * @return The SessionDAO instance
     */
    public SessionDAO forSession() {
        return sessionDAO;
    }

    /**
     * Get the DAO for ResetPassword entities
     * 
     * @return The ResetPasswordDAO instance
     */
    public ResetPasswordDAO forResetPassword() {
        return resetPasswordDAO;
    }

    /**
     * Get the DAO for PendingRegistration entities
     * 
     * @return The PendingRegistrationDAO instance
     */
    public PendingRegistrationDAO forPendingRegistration() {
        return pendingRegistrationDAO;
    }

    /**
     * Get the DAO for Workplace entities
     * 
     * @return The WorkplaceDAO instance
     */
    public WorkplaceDAO forWorkplace() {
        return workplaceDAO;
    }
}