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

@Stateless
public class AsyncEmailServiceImpl implements AsyncEmailService {
    private static final Logger logger = LogManager.getLogger(AsyncEmailServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    EmailUtil emailUtil;

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
                    String subject = "Evaluation Cycle Closed";

                    // Use EmailUtil's template system for consistent email formatting
                    String mainMessage = "Dear " + user.getFirstName() + ",\n\n" +
                            "The most recent evaluation cycle has officially closed on " + formattedClosure + ".\n\n" +
                            "You can now view your performance evaluation on your profile page. " +
                            "We encourage you to review the feedback and use it as a guide for your continued growth and development.";

                    String footerText = """
                            Thank you for your dedication and hard work.

                            If you have any questions about your evaluation, please don't hesitate to contact your manager or HR department.""";

                    // Create a dummy URL for the button (could link to profile/dashboard)
                    String profileUrl = "#"; // This could be made configurable or link to actual profile page

                    String body = emailUtil.createEmailTemplate(
                            "Evaluation Cycle Completed",
                            mainMessage,
                            "View My Profile",
                            profileUrl,
                            footerText);

                    emailUtil.sendEmail(user.getEmail(), subject, body);
                    emailsSent++;
                } catch (Exception e) {
                    logger.error("Failed to send cycle closure email to user: {} ({})", user.getEmail(), user.getId(),
                            e);
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
                    String subject = "Evaluation Cycle Started";

                    // Use EmailUtil's template system for consistent email formatting
                    String mainMessage = "Dear " + user.getFirstName() + ",\n\n" +
                            "The new evaluation cycle has officially started.\n\n" +
                            "Start Date: " + formattedStart + "\n" +
                            "End Date: " + formattedEnd + "\n\n" +
                            "Managers should complete performance evaluations for their managed users within this time frame.";

                    String footerText = "Thank you for your collaboration.\n\n" +
                            "If you have any questions about the evaluation process, please contact your manager or HR department.";

                    // Create a dummy URL for the button (could link to evaluation dashboard)
                    String evaluationUrl = "#"; // This could be made configurable or link to actual evaluation page

                    String body = emailUtil.createEmailTemplate(
                            "Evaluation Cycle Now Active",
                            mainMessage,
                            "Go to Evaluations",
                            evaluationUrl,
                            footerText);

                    emailUtil.sendEmail(user.getEmail(), subject, body);
                    emailsSent++;
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
