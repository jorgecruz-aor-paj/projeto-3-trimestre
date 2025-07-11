package aor.proj3t.unit.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.NotificationDAO;
import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.dto.paginated.NotificationPaginationDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.service.impl.NotificationServiceImpl;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.NotificationConverter;
import aor.proj3t.websocket.AppWebSocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private ConverterFactory converterFactory;

    @Mock
    private AppWebSocket webSocketService;

    @Mock
    private NotificationDAO notificationDAO;

    @Mock
    private NotificationConverter notificationConverter;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    // Test data
    private final Long NOTIFICATION_ID = 1L;
    private final Long USER_ID = 1L;
    private final String CONTENT = "Test notification content";
    private final NotificationType TYPE = NotificationType.MESSAGE;
    private final Instant NOW = Instant.now();

    private Notification testNotification;
    private NotificationDTO testNotificationDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Setup mock returns
        lenient().when(daoFactory.forNotification()).thenReturn(notificationDAO);
        lenient().when(converterFactory.forNotification()).thenReturn(notificationConverter);

        // Initialize test data
        testUser = new User();
        testUser.setId(USER_ID);

        testNotification = new Notification();
        testNotification.setId(NOTIFICATION_ID);
        testNotification.setContent(CONTENT);
        testNotification.setType(TYPE);
        testNotification.setRecipient(testUser);
        testNotification.setTimestampSent(NOW);
        testNotification.setRead(false);

        testNotificationDTO = new NotificationDTO();
        testNotificationDTO.setId(NOTIFICATION_ID);
        testNotificationDTO.setContent(CONTENT);
        testNotificationDTO.setType(TYPE);
        testNotificationDTO.setRecipientId(USER_ID);
        testNotificationDTO.setTimestampSent(NOW);
        testNotificationDTO.setRead(false);
    }

    @Test
    @DisplayName("createNotification should create and return notification with WebSocket notification")
    void createNotification_ShouldCreateAndReturnNotification() {
        // Given
        when(notificationDAO.create(any(Notification.class))).thenAnswer(i -> {
            Notification n = i.getArgument(0);
            n.setId(NOTIFICATION_ID);
            return n;
        });

        // When
        Notification result = notificationService.createNotification(testNotification);

        // Then
        assertNotNull(result);
        assertEquals(NOTIFICATION_ID, result.getId());
        assertEquals(CONTENT, result.getContent());
        assertEquals(TYPE, result.getType());
        assertFalse(result.isRead());
        assertNotNull(result.getTimestampSent());
        
        verify(notificationDAO).create(any(Notification.class));
        verify(webSocketService).notifyNewNotification(eq(USER_ID), eq(TYPE.toString()), eq(CONTENT));
    }

    @Test
    @DisplayName("getNotification should return notification DTO")
    void getNotification_ShouldReturnNotificationDTO() {
        // Given
        when(notificationDAO.findById(NOTIFICATION_ID)).thenReturn(Optional.of(testNotification));
        when(notificationConverter.toDTO(testNotification)).thenReturn(testNotificationDTO);

        // When
        NotificationDTO result = notificationService.getNotification(NOTIFICATION_ID);

        // Then
        assertNotNull(result);
        assertEquals(NOTIFICATION_ID, result.getId());
        assertEquals(CONTENT, result.getContent());
        assertEquals(TYPE, result.getType());
        assertEquals(USER_ID, result.getRecipientId());
        verify(notificationDAO).findById(NOTIFICATION_ID);
        verify(notificationConverter).toDTO(testNotification);
    }

    @Test
    @DisplayName("markAsRead should mark notification as read")
    void markAsRead_ShouldMarkNotificationAsRead() {
        // Given
        when(notificationDAO.findById(NOTIFICATION_ID)).thenReturn(Optional.of(testNotification));
        when(notificationDAO.update(any(Notification.class))).thenReturn(testNotification);

        // When
        Boolean result = notificationService.markAsRead(NOTIFICATION_ID);

        // Then
        assertTrue(result);
        assertTrue(testNotification.isRead());
        assertNotNull(testNotification.getTimestampRead());
        verify(notificationDAO).findById(NOTIFICATION_ID);
        verify(notificationDAO).update(testNotification);
    }

    @Test
    @DisplayName("markAsRead should return false when notification not found")
    void markAsRead_ShouldReturnFalse_WhenNotificationNotFound() {
        // Given
        when(notificationDAO.findById(NOTIFICATION_ID)).thenReturn(Optional.empty());

        // When
        Boolean result = notificationService.markAsRead(NOTIFICATION_ID);

        // Then
        assertFalse(result);
        verify(notificationDAO).findById(NOTIFICATION_ID);
        verify(notificationDAO, never()).update(any());
    }

    @Test
    @DisplayName("getUserNotifications should return list of notification DTOs")
    void getUserNotifications_ShouldReturnNotificationDTOs() {
        // Given
        List<Notification> notifications = Arrays.asList(testNotification);
        List<NotificationDTO> expectedDTOs = Arrays.asList(testNotificationDTO);
        
        when(notificationDAO.findByRecipientId(USER_ID)).thenReturn(notifications);
        when(notificationConverter.toDTOList(notifications)).thenReturn(expectedDTOs);

        // When
        List<NotificationDTO> result = notificationService.getUserNotifications(USER_ID);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testNotificationDTO, result.get(0));
        verify(notificationDAO).findByRecipientId(USER_ID);
        verify(notificationConverter).toDTOList(notifications);
    }

    @Test
    @DisplayName("getUserNotificationsPaginated should return paginated notification DTOs")
    void getUserNotificationsPaginated_ShouldReturnPaginatedNotifications() {
        // Given
        int page = 0;
        int size = 10;
        long totalCount = 1L;
        List<Notification> notifications = Arrays.asList(testNotification);
        List<NotificationDTO> dtos = Arrays.asList(testNotificationDTO);
        
        when(notificationDAO.findByRecipientIdPaginated(USER_ID, 0, size)).thenReturn(notifications);
        when(notificationDAO.countByRecipientId(USER_ID)).thenReturn(totalCount);
        when(notificationConverter.toDTOList(notifications)).thenReturn(dtos);

        // When
        NotificationPaginationDTO result = notificationService.getUserNotificationsPaginated(USER_ID, page, size);

        // Then
        assertNotNull(result);
        assertEquals(dtos, result.getEntities());
        assertEquals(totalCount, result.getTotalCount());
        assertEquals(size, result.getPageSize());
        assertEquals(page, result.getCurrentPage());
        assertEquals("timestampSent", result.getSortInfo().getField());
        assertFalse(result.getSortInfo().isAscending());
        
        verify(notificationDAO).findByRecipientIdPaginated(USER_ID, 0, size);
        verify(notificationDAO).countByRecipientId(USER_ID);
        verify(notificationConverter).toDTOList(notifications);
    }

    @Test
    @DisplayName("deleteNotification should delete notification when found")
    void deleteNotification_ShouldDeleteNotification_WhenFound() {
        // Given
        when(notificationDAO.findById(NOTIFICATION_ID)).thenReturn(Optional.of(testNotification));
        when(notificationDAO.deleteById(NOTIFICATION_ID)).thenReturn(true);

        // When
        Boolean result = notificationService.deleteNotification(NOTIFICATION_ID);

        // Then
        assertTrue(result);
        verify(notificationDAO).findById(NOTIFICATION_ID);
        verify(notificationDAO).deleteById(NOTIFICATION_ID);
    }

    @Test
    @DisplayName("deleteNotification should return false when notification not found")
    void deleteNotification_ShouldReturnFalse_WhenNotificationNotFound() {
        // Given
        when(notificationDAO.findById(NOTIFICATION_ID)).thenReturn(Optional.empty());

        // When
        Boolean result = notificationService.deleteNotification(NOTIFICATION_ID);

        // Then
        assertFalse(result);
        verify(notificationDAO).findById(NOTIFICATION_ID);
        verify(notificationDAO, never()).deleteById(any());
    }

    @Test
    @DisplayName("markAllAsRead should mark all user notifications as read")
    void markAllAsRead_ShouldMarkAllNotificationsAsRead() {
        // Given
        Integer expectedCount = 5;
        when(notificationDAO.markAllNotificationsAsRead(USER_ID)).thenReturn(expectedCount);

        // When
        Integer result = notificationService.markAllAsRead(USER_ID);

        // Then
        assertEquals(expectedCount, result);
        verify(notificationDAO).markAllNotificationsAsRead(USER_ID);
    }

    @Test
    @DisplayName("createNotification should handle WebSocket notification failure gracefully")
    void createNotification_ShouldHandleWebSocketFailure() {
        // Given
        when(notificationDAO.create(any(Notification.class))).thenAnswer(i -> {
            Notification n = i.getArgument(0);
            n.setId(NOTIFICATION_ID);
            return n;
        });
        doThrow(new RuntimeException("WebSocket error")).when(webSocketService)
            .notifyNewNotification(any(), any(), any());

        // When
        Notification result = notificationService.createNotification(testNotification);

        // Then
        assertNotNull(result);
        assertEquals(NOTIFICATION_ID, result.getId());
        verify(notificationDAO).create(any(Notification.class));
        verify(webSocketService).notifyNewNotification(eq(USER_ID), eq(TYPE.toString()), eq(CONTENT));
    }
}
