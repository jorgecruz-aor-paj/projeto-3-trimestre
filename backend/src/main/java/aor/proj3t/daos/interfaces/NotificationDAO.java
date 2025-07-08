package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.Notification;

public interface NotificationDAO extends BasicDAO<Notification, Long> {

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
     * Marks all notifications as read for a specific user.
     *
     * @param recipientId The ID of the user.
     * @return The number of notifications marked as read.
     */
    Integer markAllNotificationsAsRead(Long recipientId);
}
