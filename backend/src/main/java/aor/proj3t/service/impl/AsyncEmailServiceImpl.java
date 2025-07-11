package aor.proj3t.service.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.service.interfaces.AsyncEmailService;
import aor.proj3t.util.EmailUtil;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

/**
 * Implementation of the AsyncEmailService interface.
 * This service provides asynchronous email sending capabilities for system
 * notifications,
 * particularly for evaluation cycle closure notifications and bulk email
 * operations.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Asynchronous evaluation cycle closure notifications</li>
 * <li>Bulk email sending to all system users</li>
 * <li>Email delivery tracking and error handling</li>
 * <li>Formatted date and user-specific email content</li>
 * </ul>
 * 
 * <p>
 * This service uses EJB asynchronous processing to handle time-consuming
 * email operations without blocking the main application flow. It includes
 * comprehensive error handling and logging for email delivery monitoring.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
@Stateless
public class AsyncEmailServiceImpl implements AsyncEmailService {
    private static final Logger logger = LogManager.getLogger(AsyncEmailServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    EmailUtil emailUtil;

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Implementation note: Runs asynchronously to avoid blocking the calling
     * thread,
     * making it suitable for bulk email operations that may take considerable time.
     */
    @Asynchronous
    @Override
    public void sendCycleClosureEmailAsync(Long cycleId) {
        try {
            EvaluationCycle cycle = daoFactory.forEvaluationCycle().findById(cycleId).orElse(null);
            if (cycle == null) {
                logger.error("Evaluation cycle not found with id: {}", cycleId);
                return;
            }

            logger.info("Starting to send cycle closure emails for cycle: {}", cycleId);

            List<User> userList = daoFactory.forUser().findAll();
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd MMMM yyyy")
                    .withZone(ZoneId.systemDefault());
            String formattedClosure = formatter.format(cycle.getClosureDate());

            int emailsSent = 0;
            for (User user : userList) {
                try {
                    if (emailUtil.sendCycleClosureEmail(user.getEmail(), user.getFirstName(), formattedClosure)) {
                        emailsSent++;
                    }
                } catch (Exception e) {
                    logger.error("Failed to send cycle closure email to user: {} ({})",
                            user.getEmail(),
                            user.getId(), e);
                }
            }

            logger.info("Successfully sent {} cycle closure emails for cycle: {}", emailsSent, cycleId);
        } catch (Exception e) {
            logger.error("Failed to send cycle closure emails for cycle: {}", cycleId, e);
        }
    }

    @Asynchronous
    @Override
    public void sendCycleStartEmailAsync(Long cycleId) {
        try {
            EvaluationCycle cycle = daoFactory.forEvaluationCycle().findById(cycleId).orElse(null);
            if (cycle == null) {
                logger.error("Evaluation cycle not found with id: {}", cycleId);
                return;
            }

            logger.info("Starting to send cycle start emails for cycle: {}", cycleId);

            List<User> userList = daoFactory.forUser().getManagers();
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd MMMM yyyy")
                    .withZone(ZoneId.systemDefault());
            String formattedStart = formatter.format(cycle.getStartDate());
            String formattedEnd = formatter.format(cycle.getEndDate());

            int emailsSent = 0;
            for (User user : userList) {
                try {
                    if (emailUtil.sendCycleStartEmail(user.getEmail(), user.getFirstName(), formattedStart,
                            formattedEnd)) {
                        emailsSent++;
                    }
                } catch (Exception e) {
                    logger.error("Failed to send cycle start email to user: {} ({})", user.getEmail(), user.getId(), e);
                }
            }

            logger.info("Successfully sent {} cycle start emails for cycle: {}", emailsSent, cycleId);
        } catch (Exception e) {
            logger.error("Failed to send cycle start emails for cycle: {}", cycleId, e);
        }
    }
}
