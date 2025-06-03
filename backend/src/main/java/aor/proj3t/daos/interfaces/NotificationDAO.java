package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.Message;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;

import java.util.List;

public interface NotificationDAO extends BasicDAO<Notification, Long> {

    /**
     * Finds all notifications sent by a specific user.
     *
     * @param senderId The ID of the sender.
     * @return A list of notifications sent by the specified user.
     */
    List<Message> findBySenderId(Long senderId);

    /**
     * Finds all notifications received by a specific user.
     *
     * @param recipientId The ID of the recipient.
     * @return A list of notifications received by the specified user.
     */
    List<Message> findByRecipientId(Long recipientId);


    /**
     * Counts the number of unread notifications of a user.
     *
     * @param recipientId The ID of the user.
     * @return A count of unread notifications.
     */
    Integer findCountUnreadnotificationsByRecipientId(Long recipientId);

    /**
     * Finds all notifications that a given user has still not read.
     *
     * @param recipientId The ID of the user.
     * @return A list of unique notifications unread by the user.
     */
    List<Message> findUnreadnotificationsByRecipientId(Long recipientId);

    /**
     * Marks a notification has read.
     *
     * @param notificationId The ID of the notification.
     * @return True for success and False for unsuccessful marking.
     */
    Boolean markMessageAsRead(Long notificationId);

}
