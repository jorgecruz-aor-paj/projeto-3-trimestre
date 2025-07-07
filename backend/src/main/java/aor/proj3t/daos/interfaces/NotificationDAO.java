package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.Notification;

public interface NotificationDAO extends BasicDAO<Notification, Long> {

    /**
     * Finds all notifications sent by a specific user.
     *
     * @param senderId The ID of the sender.
     * @return A list of notifications sent by the specified user.
     */
    List<Notification> findBySenderId(Long senderId);

    /**
     * Finds all notifications received by a specific user.
     *
     * @param recipientId The ID of the recipient.
     * @return A list of notifications received by the specified user.
     */
    List<Notification> findByRecipientId(Long recipientId);

    /**
     * Finds notifications received by a specific user with pagination and sorting.
     *
     * @param recipientId The ID of the recipient.
     * @param offset      The number of results to skip (for pagination).
     * @param limit       The maximum number of results to return.
     * @return A list of notifications received by the specified user.
     */
    List<Notification> findByRecipientIdPaginated(Long recipientId, int offset, int limit);

    /**
     * Counts the total number of notifications received by a specific user.
     *
     * @param recipientId The ID of the recipient.
     * @return The total count of notifications for the user.
     */
    Long countByRecipientId(Long recipientId);

    /**
     * Counts the number of unread notifications of a user.
     *
     * @param recipientId The ID of the user.
     * @return A count of unread notifications.
     */
    Long countUnreadNotificationsByRecipientId(Long recipientId);

    /**
     * Finds all notifications that a given user has still not read.
     *
     * @param recipientId The ID of the user.
     * @return A list of unique notifications unread by the user.
     */
    List<Notification> findUnreadNotificationsByRecipientId(Long recipientId);

    /**
     * Marks a notification has read.
     *
     * @param notificationId The ID of the notification.
     * @return True for success and False for unsuccessful marking.
     */
    Boolean markNotificationAsRead(Long notificationId);

}
