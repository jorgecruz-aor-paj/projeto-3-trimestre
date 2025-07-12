package aor.proj3t.util;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Utility class for creating notification content strings.
 * Supports both simple text notifications and structured JSON notifications for
 * i18n.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class NotificationHelper {

    /**
     * Creates a JSON notification content string for message notifications
     * 
     * @param key             Translation key
     * @param senderFirstName Sender's first name
     * @param senderLastName  Sender's last name
     * @return JSON string for notification content
     */
    public String createNotificationForMessageContent(String key, String senderFirstName, String senderLastName) {
        return String.format(
                "{\"key\":\"%s\",\"senderFirstName\":\"%s\",\"senderLastName\":\"%s\"}",
                key,
                senderFirstName,
                senderLastName);
    }

    /**
     * Creates a JSON notification content string for evaluation notifications
     * 
     * @param key                Translation key
     * @param evaluatorFirstName Evaluator's first name
     * @param evaluatorLastName  Evaluator's last name
     * @return JSON string for notification content
     */
    public String createNotificationForEvaluationContent(String key, String evaluatorFirstName,
            String evaluatorLastName) {
        return String.format(
                "{\"key\":\"%s\",\"evaluatorFirstName\":\"%s\",\"evaluatorLastName\":\"%s\"}",
                key,
                evaluatorFirstName,
                evaluatorLastName);
    }

    /**
     * Creates a JSON notification content string for training notifications
     * 
     * @param key           Translation key
     * @param trainingTitle Training title
     * @return JSON string for notification content
     */
    public String createNotificationForTrainingContent(String key, String trainingTitle) {
        return String.format(
                "{\"key\":\"%s\",\"trainingTitle\":\"%s\"}",
                key,
                trainingTitle);
    }

    /**
     * Creates a JSON notification content string for training participation
     * notifications
     * 
     * @param key              Translation key
     * @param trainingTitle    Training title
     * @param managerFirstName Manager's first name
     * @param managerLastName  Manager's last name
     * @return JSON string for notification content
     */
    public String createNotificationForTrainingParticipationContent(String key, String trainingTitle,
            String managerFirstName, String managerLastName) {
        return String.format(
                "{\"key\":\"%s\",\"trainingTitle\":\"%s\",\"managerFirstName\":\"%s\",\"managerLastName\":\"%s\"}",
                key,
                trainingTitle,
                managerFirstName,
                managerLastName);
    }

    /**
     * Creates a JSON notification content string for evaluation completion with
     * score
     * 
     * @param key                Translation key
     * @param evaluatorFirstName Evaluator's first name
     * @param evaluatorLastName  Evaluator's last name
     * @param subject            Subject of evaluation
     * @param score              Score obtained
     * @return JSON string for notification content
     */
    public String createNotificationForEvaluationWithScore(String key, String evaluatorFirstName,
            String evaluatorLastName, String subject, String score) {
        return String.format(
                "{\"key\":\"%s\",\"evaluatorFirstName\":\"%s\",\"evaluatorLastName\":\"%s\",\"subject\":\"%s\",\"score\":\"%s\"}",
                key,
                evaluatorFirstName,
                evaluatorLastName,
                subject,
                score);
    }

    /**
     * Creates a JSON notification content string for evaluation cycle notifications
     * 
     * @param key     Translation key
     * @param cycleId Evaluation cycle ID
     * @return JSON string for notification content
     */
    public String createNotificationForEvaluationCycleContent(String key, String cycleId) {
        return String.format(
                "{\"key\":\"%s\",\"cycleId\":\"%s\"}",
                key,
                cycleId);
    }

    /**
     * Creates a simple text notification content (for backward compatibility)
     * 
     * @param text Plain text content
     * @return The same text (for consistency)
     */
    public String createSimpleNotificationContent(String text) {
        return text;
    }
}
