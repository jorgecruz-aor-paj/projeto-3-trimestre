package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.enums.NotificationType;

/**
 * Service interface for Notification operations
 */
public interface NotificationService {

    /**
     * Creates a new notification
     * 
     * @param notification The notification entity to create
     * @return The created notification
     */
    Notification createNotification(Notification notification);

    /**
     * Retrieves a notification by its ID
     * 
     * @param notificationId The ID of the notification to retrieve
     * @return The notification DTO
     */
    NotificationDTO getNotification(Long notificationId);

    /**
     * Updates a notification's read status
     * 
     * @param notificationId The ID of the notification to mark as read
     * @return true if successful, false otherwise
     */
    Boolean markAsRead(Long notificationId);

    /**
     * Retrieves all notifications for a user
     * 
     * @param userId The user ID
     * @return List of notification DTOs
     */
    List<NotificationDTO> getUserNotifications(Long userId);

    /**
     * Retrieves all notifications of a specific type for a user
     * 
     * @param userId The user ID
     * @param type   The notification type
     * @return List of notification DTOs
     */
    List<NotificationDTO> getUserNotificationsByType(Long userId, NotificationType type);

    /**
     * Deletes a notification
     * 
     * @param notificationId The ID of the notification to delete
     * @return true if successful, false otherwise
     */
    Boolean deleteNotification(Long notificationId);

    /**
     * Marks all notifications for a user as read
     * 
     * @param userId The user ID
     * @return Number of notifications marked as read
     */
    Integer markAllAsRead(Long userId);

}
