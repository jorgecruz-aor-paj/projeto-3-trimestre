package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.Notification;

import java.util.List;

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
