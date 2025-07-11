package aor.proj3t.service.interfaces;

import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.dto.paginated.NotificationPaginationDTO;
import aor.proj3t.entities.Notification;

import java.util.List;

/**
 * Service interface for notification management operations.
 * This interface defines the contract for comprehensive notification lifecycle
 * management
 * including creation, delivery, read status tracking, and real-time WebSocket
 * integration.
 * 
 * <p>
 * Key notification services include:
 * <ul>
 * <li><b>Notification Creation:</b> Creating and persisting notification
 * entities</li>
 * <li><b>Real-time Delivery:</b> WebSocket integration for immediate
 * notification delivery</li>
 * <li><b>Read Status Management:</b> Tracking and updating notification read
 * states</li>
 * <li><b>User Notification Retrieval:</b> Fetching user-specific notifications
 * with pagination</li>
 * <li><b>Bulk Operations:</b> Mass read status updates and notification
 * management</li>
 * <li><b>Notification Cleanup:</b> Deletion and archival of notifications</li>
 * </ul>
 * 
 * <p>
 * This service integrates with WebSocket services to provide real-time
 * notification
 * delivery and maintains comprehensive notification tracking for audit and user
 * experience purposes.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
public interface NotificationService {

    /**
     * Creates a new notification and sends real-time WebSocket notification to the
     * recipient.
     * Sets timestamp, marks as unread, persists the notification, and attempts
     * WebSocket delivery.
     * 
     * @param notification the notification entity to create and persist
     * @return the persisted notification with generated ID and timestamps
     */
    Notification createNotification(Notification notification);

    /**
     * Retrieves a notification by its unique identifier and converts it to DTO
     * format.
     * 
     * @param notificationId the unique identifier of the notification to retrieve
     * @return the notification DTO containing the notification data
     * @throws NotFoundException if no notification exists with the given ID
     */
    NotificationDTO getNotification(Long notificationId);

    /**
     * Marks a notification as read and records the timestamp when it was read.
     * Updates the notification status and provides user feedback.
     * 
     * @param notificationId the unique identifier of the notification to mark as
     *                       read
     * @return true if the notification was successfully marked as read, false if
     *         not found
     */
    Boolean markAsRead(Long notificationId);

    /**
     * Retrieves all notifications for a specific user.
     * Returns comprehensive notification history for the user.
     * 
     * @param userId the unique identifier of the user whose notifications to
     *               retrieve
     * @return list of NotificationDTO objects representing the user's notifications
     */
    List<NotificationDTO> getUserNotifications(Long userId);

    /**
     * Retrieves notifications for a user with pagination support.
     * Returns paginated results with total count and default sorting by timestamp
     * (newest first).
     * 
     * @param userId the unique identifier of the user whose notifications to
     *               retrieve
     * @param page   the page number to retrieve (0-based)
     * @param size   the number of notifications per page
     * @return NotificationPaginationDTO containing the paginated results and
     *         metadata
     */
    NotificationPaginationDTO getUserNotificationsPaginated(Long userId, int page, int size);

    /**
     * Deletes a notification by its unique identifier.
     * Handles cases where the notification might not exist gracefully.
     * 
     * @param notificationId the unique identifier of the notification to delete
     * @return true if the notification was successfully deleted, false if not found
     *         or deletion failed
     */
    Boolean deleteNotification(Long notificationId);

    /**
     * Marks all notifications for a user as read in a bulk operation.
     * Efficiently updates multiple notification read statuses for improved user
     * experience.
     * 
     * @param userId the unique identifier of the user whose notifications should be
     *               marked as read
     * @return the number of notifications that were successfully marked as read
     */
    Integer markAllAsRead(Long userId);

}
