package aor.proj3t.service.interfaces;

import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.dto.paginated.NotificationPaginationDTO;
import aor.proj3t.entities.Notification;

import java.util.List;

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
     * Retrieves notifications for a user with pagination
     * 
     * @param userId The user ID
     * @param page   The page number (0-based)
     * @param size   The page size
     * @return Paginated notification DTOs
     */
    NotificationPaginationDTO getUserNotificationsPaginated(Long userId, int page, int size);

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
