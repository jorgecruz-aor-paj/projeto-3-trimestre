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
                    String body = "<h1>Evaluation Cycle Completed</h1>" +
                            "<p>Dear " + user.getFirstName() + ",</p>" +
                            "<p>The most recent evaluation cycle has officially closed on <strong>" + formattedClosure
                            + "</strong>.</p>" +
                            "<p>You can now view your performance evaluation on your profile page.</p>" +
                            "<p>We encourage you to review the feedback and use it as a guide for your continued growth and development.</p>"
                            +
                            "<br>" +
                            "<p>Thank you for your dedication and hard work.</p>";

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
}
