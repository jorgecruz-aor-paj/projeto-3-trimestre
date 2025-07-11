package aor.proj3t.enums;

/**
 * Enum representing the different types of notifications that can be sent to
 * users.
 * Each type corresponds to a specific kind of system event or user action
 * that triggers a notification.
 * 
 * @author Project Team
 * @version 1.0
 * @since 1.0
 */
public enum NotificationType {
    /** Notification for new messages */
    MESSAGE,

    /** Notification for content edits */
    EDIT,

    /** Notification for content deletions */
    DELETE,

    /** Notification for evaluation-related events */
    EVALUATION,

    /** Notification for evaluation cycle events */
    CYCLE
}
