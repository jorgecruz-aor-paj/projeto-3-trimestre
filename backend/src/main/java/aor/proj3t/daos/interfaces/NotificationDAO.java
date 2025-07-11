package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.Notification;

/**
 * Data Access Object interface for Notification entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide comprehensive operations
 * for managing system notifications. Notifications inform users about important
 * events, updates, and actions that require their attention within the
 * application.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Querying notifications by recipient</li>
 * <li>Paginated notification retrieval for performance</li>
 * <li>Counting total notifications for pagination controls</li>
 * <li>Bulk marking notifications as read</li>
 * <li>Notification management and cleanup operations</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see Notification
 * @see BasicDAO
 */
public interface NotificationDAO extends BasicDAO<Notification, Long> {

    /**
     * Retrieves all notifications received by a specific user.
     * <p>
     * This method finds all notifications where the specified user is the
     * recipient.
     * Notifications are typically ordered by creation time with most recent first.
     * For better performance with large datasets, consider using the paginated
     * version.
     * </p>
     *
     * @param recipientId The unique identifier of the recipient. Must not be null.
     * @return A {@link List} of notifications received by the specified user,
     *         or an empty list if no notifications are found
     * @throws IllegalArgumentException               if recipientId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     * @see #findByRecipientIdPaginated(Long, int, int)
     */
    List<Notification> findByRecipientId(Long recipientId);

    /**
     * Retrieves notifications for a specific user with pagination and sorting.
     * <p>
     * This method provides paginated access to notifications, which is essential
     * for performance when users have large numbers of notifications. Results
     * are typically sorted by creation time with most recent first.
     * </p>
     *
     * @param recipientId The unique identifier of the recipient. Must not be null.
     * @param offset      The number of results to skip (for pagination). Must be >=
     *                    0.
     * @param limit       The maximum number of results to return. Must be > 0.
     * @return A {@link List} of notifications received by the specified user,
     *         or an empty list if no notifications are found
     * @throws IllegalArgumentException               if recipientId is null, offset
     *                                                < 0, or limit <= 0
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<Notification> findByRecipientIdPaginated(Long recipientId, int offset, int limit);

    /**
     * Counts the total number of notifications received by a specific user.
     * <p>
     * This method provides the total count of notifications for a user, which is
     * essential for implementing pagination controls and displaying total counts
     * in user interfaces.
     * </p>
     *
     * @param recipientId The unique identifier of the recipient. Must not be null.
     * @return The total count of notifications for the user, or 0 if none exist
     * @throws IllegalArgumentException               if recipientId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Long countByRecipientId(Long recipientId);

    /**
     * Marks all notifications as read for a specific user.
     * <p>
     * This bulk operation updates the read status of all notifications for the
     * specified user. Commonly used when users want to clear all unread
     * notification indicators or during "mark all as read" operations.
     * </p>
     *
     * @param recipientId The unique identifier of the user. Must not be null.
     * @return The number of notifications that were marked as read
     * @throws IllegalArgumentException               if recipientId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Integer markAllNotificationsAsRead(Long recipientId);
}
