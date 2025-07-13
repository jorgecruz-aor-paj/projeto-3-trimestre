package aor.proj3t.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.InvalidDataException;
import aor.proj3t.filters.DateRange;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.EvaluationCycleService;
import aor.proj3t.util.EmailUtil;
import aor.proj3t.util.NotificationHelper;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implementation of the EvaluationCycleService interface.
 * This service manages the complete lifecycle of evaluation cycles including
 * creation,
 * management, closure, and user notification workflows for performance
 * evaluation periods.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Evaluation cycle creation with date validation</li>
 * <li>Bulk evaluation creation for all managed users</li>
 * <li>Cycle closure and finalization processes</li>
 * <li>Email notification workflows for cycle events</li>
 * <li>Validation of user management requirements</li>
 * <li>Integration with asynchronous email services</li>
 * </ul>
 * 
 * <p>
 * This service ensures proper evaluation cycle management by validating
 * that all users have assigned managers before starting cycles, creating
 * evaluations for all eligible users, and managing the complete cycle workflow.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class EvaluationCycleServiceImpl extends BaseService implements EvaluationCycleService {
    private static final Logger logger = LogManager.getLogger(EvaluationCycleServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    private ConverterFactory converterFactory;

    @Inject
    private EmailUtil emailUtil;

    @Inject
    private ServiceFactory serviceFactory;

    @Inject
    private NotificationHelper notificationHelper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void startCycle(Long requesterId, DateRange dateRange) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        if (daoFactory.forUser().countUsersWithoutManager() != 0) {
            throw new InvalidDataException(
                    "There are users without manager",
                    "Admin with id " + requesterId + " started evaluation cycle with unmanaged users");
        }
        if (daoFactory.forEvaluationCycle().countActiveEvaluationCycles() > 0) {
            throw new InvalidDataException(
                    "There is an active evaluation cycle running",
                    "Admin with id " + requesterId + " started evaluation cycle while another is active");
        }
        EvaluationCycle evaluationCycle = new EvaluationCycle(dateRange.getStartDate(), dateRange.getEndDate(),
                requester);
        evaluationCycle = daoFactory.forEvaluationCycle().create(evaluationCycle);
        logger.info("Created evaluation cycle with id {}", evaluationCycle.getId());
        createNewEvaluations(evaluationCycle);
        logger.info("Created evaluations for evaluation cycle with id {}", evaluationCycle.getId());

        // Send cycle start emails asynchronously to avoid transaction timeout
        serviceFactory.forAsyncEmail().sendCycleStartEmailAsync(evaluationCycle.getId());
        logger.info("Initiated async cycle start emails for evaluation cycle with id {}", evaluationCycle.getId());
    }

    // No JavaDoc needed - inherits from interface automatically
    @Override
    @Transactional
    public EvaluationCycleDTO getActiveCycle(Long requesterId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        List<EvaluationCycle> cycles = daoFactory.forEvaluationCycle().findActive(true);
        if (cycles.isEmpty()) {
            return new EvaluationCycleDTO();
        }
        if (cycles.size() > 1) {
            throw new ConflictException(
                    "Two or more active evaluation cycles found",
                    "Multiple active evaluation cycles exist — cannot determine the correct one");
        }
        logger.info("Found active evaluation cycle with id {}", cycles.getFirst().getId());
        return converterFactory.forEvaluationCycle().toDTO(cycles.getFirst());
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Implementation note: This method performs admin authorization check and
     * retrieves cycles using the DAO layer with optional evaluation loading.
     */
    @Override
    public List<EvaluationCycleDTO> getAllCycles(Long requesterId, boolean withEvaluations) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        List<EvaluationCycle> cycles = daoFactory.forEvaluationCycle().findAll(withEvaluations);
        logger.info("Found {} active evaluation cycles for user with id {}", cycles.size(), requesterId);
        return converterFactory.forEvaluationCycle().toDTOList(cycles);
    }

    private void createNewEvaluations(EvaluationCycle evaluationCycle) {
        List<User> users = daoFactory.forUser().findAll();
        for (User user : users) {
            if (!user.getRole().equals(Role.ADMIN)) {
                Evaluation evaluation = new Evaluation(evaluationCycle, user, user.getManager());
                daoFactory.forEvaluation().create(evaluation);
            }
        }
        logger.info(" {} evaluations created in evaluation cycle with id {}", users.size(),
                evaluationCycle.getId());

    }

    // Inherits JavaDoc from interface
    // TODO Método só chamado em testes.
    @Override
    public void sendCycleClosureEmail(EvaluationCycle cycle) {
        List<User> userList = daoFactory.forUser().findAll();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd MMMM yyyy")
                .withZone(ZoneId.systemDefault());
        String formattedClosure = formatter.format(cycle.getClosureDate());

        for (User user : userList) {
            String subject = "Evaluation Cycle Closed";

            // Use EmailUtil's template system for consistent email formatting
            String mainMessage = "Dear " + user.getFirstName() + ",\n\n" +
                    "The most recent evaluation cycle has officially closed on " + formattedClosure + ".\n\n" +
                    "You can now view your performance evaluation on your profile page. " +
                    "We encourage you to review the feedback and use it as a guide for your continued growth and development.";

            String footerText = "Thank you for your dedication and hard work.\n\n" +
                    "If you have any questions about your evaluation, please don't hesitate to contact your manager or HR department.";

            // Create a dummy URL for the button (could link to profile/dashboard)
            String profileUrl = "#"; // This could be made configurable or link to actual profile page

            String body = emailUtil.createEmailTemplate(
                    "Evaluation Cycle Completed",
                    mainMessage,
                    "View My Profile",
                    profileUrl,
                    footerText);

            emailUtil.sendEmail(user.getEmail(), subject, body);
        }
    }

    // Inherits JavaDoc from interface
    @Override
    public void checkAndCloseEvaluationCycle(Long evaluationCycleId) {
        if (daoFactory.forEvaluation().countUnclosedEvaluationsByCycleId(evaluationCycleId) > 0) {
            return;
        }
        EvaluationCycle cycle = getEntity(daoFactory.forEvaluationCycle().findById(evaluationCycleId),
                evaluationCycleId, "EvaluationCycle");
        cycle.setStatus(EvaluationCycleStatus.CLOSED);
        cycle.setClosureDate(Instant.now());
        daoFactory.forEvaluationCycle().update(cycle);
        logger.info("Evaluation cycle with id {} has been closed", evaluationCycleId);

        // Send emails asynchronously to avoid transaction timeout
        serviceFactory.forAsyncEmail().sendCycleClosureEmailAsync(evaluationCycleId);
    }

    // Inherits JavaDoc from interface
    @Override
    public void checkAndCompleteEvaluationCycle(Long evaluationCycleId) {
        if (daoFactory.forEvaluation().countOngoingEvaluationsByCycle(evaluationCycleId) > 0) {
            return;
        }
        daoFactory.forEvaluationCycle().updateStatus(evaluationCycleId, EvaluationCycleStatus.COMPLETED);
        logger.info("Evaluation cycle with id {} has been completed", evaluationCycleId);

        // Send notification to admin responsible for cycle
        try {
            EvaluationCycle cycle = getEntity(daoFactory.forEvaluationCycle().findById(evaluationCycleId),
                    evaluationCycleId, "EvaluationCycle");
            User admin = cycle.getCreatedBy();
            if (admin != null) {
                Notification notification = new Notification();
                notification.setSender(admin);
                notification.setRecipient(admin);
                notification.setType(NotificationType.CYCLE);
                notification.setContent(notificationHelper.createNotificationForEvaluationCycleContent(
                        "notifications.evaluationCycle.completed",
                        evaluationCycleId.toString()));
                serviceFactory.forNotification().createNotification(notification);
            }
        } catch (Exception e) {
            logger.error("Failed to send notification for completed evaluation cycle {}", evaluationCycleId, e);
        }
    }

}
