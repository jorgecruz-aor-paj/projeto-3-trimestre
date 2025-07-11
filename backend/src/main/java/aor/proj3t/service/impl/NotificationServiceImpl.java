package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.dto.paginated.NotificationPaginationDTO;
import aor.proj3t.dto.paginated.SortInfo;
import aor.proj3t.entities.Notification;
import aor.proj3t.service.interfaces.NotificationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.websocket.AppWebSocket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implementation of the NotificationService interface.
 * This service manages all notification-related operations including creation,
 * retrieval,
 * marking as read, and real-time WebSocket notifications to users.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Creating and persisting notifications</li>
 * <li>Sending real-time WebSocket notifications</li>
 * <li>Marking notifications as read with timestamps</li>
 * <li>Retrieving paginated notification lists for users</li>
 * <li>Converting between entity and DTO representations</li>
 * </ul>
 * 
 * <p>
 * This service integrates with WebSocket services to provide real-time
 * notification delivery to connected users.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class NotificationServiceImpl extends BaseService implements NotificationService {

    private static final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

    @Inject
    private DAOFactory daoFactory;

    @Inject
    private ConverterFactory converterFactory;

    @Inject
    private AppWebSocket webSocketService;

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Implementation note: Creates notification with timestamp, persists to
     * database,
     * and sends real-time WebSocket notification to recipient.
     */
    @Override
    @Transactional
    public Notification createNotification(Notification notification) {
        notification.setTimestampSent(Instant.now());
        notification.setRead(false);
        daoFactory.forNotification().create(notification);
        logger.info("Notification created with ID: {}", notification.getId());

        // Send WebSocket notification to recipient
        try {
            webSocketService.notifyNewNotification(
                    notification.getRecipient().getId(),
                    notification.getType().toString(),
                    notification.getContent());
        } catch (Exception e) {
            logger.error("Failed to send WebSocket notification", e);
        }

        return notification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationDTO getNotification(Long notificationId) {
        Optional<Notification> notificationOpt = daoFactory.forNotification().findById(notificationId);
        Notification notification = getEntity(notificationOpt, notificationId, "Notification");
        return converterFactory.forNotification().toDTO(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Boolean markAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = daoFactory.forNotification().findById(notificationId);
        if (!notificationOpt.isPresent()) {
            logger.warn("Notification with ID: {} not found", notificationId);
            return false;
        }

        Notification notification = notificationOpt.get();
        notification.setRead(true);
        notification.setTimestampRead(Instant.now());
        daoFactory.forNotification().update(notification);
        logger.info("Notification with ID: {} marked as read", notificationId);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        List<Notification> notifications = daoFactory.forNotification().findByRecipientId(userId);
        return converterFactory.forNotification().toDTOList(notifications);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationPaginationDTO getUserNotificationsPaginated(Long userId, int page, int size) {
        int offset = page * size;
        List<Notification> notifications = daoFactory.forNotification().findByRecipientIdPaginated(userId, offset,
                size);
        Long totalCount = daoFactory.forNotification().countByRecipientId(userId);

        List<NotificationDTO> notificationDTOs = converterFactory.forNotification().toDTOList(notifications);
        SortInfo sortInfo = new SortInfo("timestampSent", false); // false for descending order

        logger.info("Retrieved {} notifications for user ID: {} (page {}, size {})",
                notificationDTOs.size(), userId, page, size);

        return new NotificationPaginationDTO(notificationDTOs, totalCount, size, page, sortInfo);
    }

    /**
     * Deletes a notification by its unique identifier.
     * This method handles the case where the notification might not exist
     * gracefully.
     * 
     * @param notificationId the unique identifier of the notification to delete
     * @return true if the notification was successfully deleted, false if not found
     *         or deletion failed
     */
    @Override
    @Transactional
    public Boolean deleteNotification(Long notificationId) {
        Optional<Notification> notificationOpt = daoFactory.forNotification().findById(notificationId);
        if (!notificationOpt.isPresent()) {
            logger.warn("Notification with ID: {} not found for deletion", notificationId);
            return false;
        }

        boolean success = daoFactory.forNotification().deleteById(notificationId);
        if (success) {
            logger.info("Notification with ID: {} deleted", notificationId);
        } else {
            logger.error("Failed to delete notification with ID: {}", notificationId);
        }
        return success;
    }

    @Override
    @Transactional
    public Integer markAllAsRead(Long userId) {
        Integer count = daoFactory.forNotification().markAllNotificationsAsRead(userId);
        logger.info("{} notifications marked as read for user ID: {}", count, userId);
        return count;
    }
}
