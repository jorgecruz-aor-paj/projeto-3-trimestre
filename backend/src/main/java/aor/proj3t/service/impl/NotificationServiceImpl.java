package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.dto.paginated.NotificationPaginationDTO;
import aor.proj3t.dto.paginated.SortInfo;
import aor.proj3t.entities.Notification;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.service.interfaces.NotificationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implementation of the NotificationService interface
 */
@ApplicationScoped
public class NotificationServiceImpl extends BaseService implements NotificationService {

    private static final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

    @Inject
    private DAOFactory daoFactory;

    @Inject
    private ConverterFactory converterFactory;

    @Override
    @Transactional
    public Notification createNotification(Notification notification) {
        notification.setTimestampSent(Instant.now());
        notification.setRead(false);
        daoFactory.forNotification().create(notification);
        logger.info("Notification created with ID: {}", notification.getId());
        return notification;
    }

    @Override
    public NotificationDTO getNotification(Long notificationId) {
        Optional<Notification> notificationOpt = daoFactory.forNotification().findById(notificationId);
        Notification notification = getEntity(notificationOpt, notificationId, "Notification");
        return converterFactory.forNotification().toDTO(notification);
    }

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

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        List<Notification> notifications = daoFactory.forNotification().findByRecipientId(userId);
        return converterFactory.forNotification().toDTOList(notifications);
    }

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

    @Override
    public List<NotificationDTO> getUserNotificationsByType(Long userId, NotificationType type) {
        List<Notification> notifications = daoFactory.forNotification().findByRecipientId(userId);

        // Filter by type
        List<Notification> filteredNotifications = notifications.stream()
                .filter(n -> n.getType() == type)
                .collect(Collectors.toList());

        return converterFactory.forNotification().toDTOList(filteredNotifications);
    }

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
        List<Notification> unreadNotifications = daoFactory.forNotification().findByRecipientId(userId).stream()
                .filter(n -> !n.isRead())
                .collect(Collectors.toList());

        int count = 0;
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notification.setTimestampRead(Instant.now());
            daoFactory.forNotification().update(notification);
            count++;
        }

        logger.info("{} notifications marked as read for user ID: {}", count, userId);
        return count;
    }
}
