package aor.proj3t.util.converterutils;

import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import aor.proj3t.enums.NotificationType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NotificationConverter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationConverter Tests")
public class NotificationConverterTest extends BaseConverterTest<Notification, NotificationDTO, NotificationConverter> {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private NotificationConverter notificationConverter;

    private Notification notification;
    private NotificationDTO notificationDTO;
    private User mockSender;
    private User mockRecipient;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(User.class), any(Long.class))).thenReturn(null);
        
        // Create mock users
        mockSender = new User();
        mockSender.setId(1L);
        mockSender.setEmail("sender@example.com");

        mockRecipient = new User();
        mockRecipient.setId(2L);
        mockRecipient.setEmail("recipient@example.com");

        // Create test notification
        notification = new Notification();
        notification.setId(1L);
        notification.setContent("This is a test notification");
        notification.setType(NotificationType.MESSAGE);
        notification.setRead(false);
        notification.setTimestampSent(Instant.now());
        notification.setTimestampRead(null);
        notification.setSender(mockSender);
        notification.setRecipient(mockRecipient);

        // Create test DTO
        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setContent("This is a test notification");
        notificationDTO.setType(NotificationType.MESSAGE);
        notificationDTO.setRead(false);
        notificationDTO.setTimestampSent(Instant.now());
        notificationDTO.setTimestampRead(null);
        notificationDTO.setSenderId(1L);
        notificationDTO.setRecipientId(2L);
    }

    @Override
    protected NotificationConverter getConverter() {
        return notificationConverter;
    }

    @Override
    protected Notification createValidEntity() {
        Notification entity = new Notification();
        entity.setId(1L);
        entity.setContent("This is a test notification");
        entity.setType(NotificationType.MESSAGE);
        entity.setRead(false);
        entity.setTimestampSent(Instant.now());
        entity.setTimestampRead(null);
        // Don't set sender/recipient for base test
        return entity;
    }

    @Override
    protected NotificationDTO createValidDTO() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);
        dto.setContent("This is a test notification");
        dto.setType(NotificationType.MESSAGE);
        dto.setRead(false);
        dto.setTimestampSent(Instant.now());
        dto.setTimestampRead(null);
        // Don't set senderId/recipientId for base test
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(Notification entity, NotificationDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getContent(), dto.getContent());
        assertEquals(entity.getType(), dto.getType());
        assertEquals(entity.isRead(), dto.isRead());
        assertEquals(entity.getTimestampSent(), dto.getTimestampSent());
        assertEquals(entity.getTimestampRead(), dto.getTimestampRead());
        // Note: Not checking sender/recipient relationships in base test
    }

    @Override
    protected void assertDTOEqualsEntity(NotificationDTO dto, Notification entity) {
        // Don't check ID as it comes from database, not DTO
        assertEquals(dto.getContent(), entity.getContent());
        assertEquals(dto.getType(), entity.getType());
        assertEquals(dto.isRead(), entity.isRead());
        assertEquals(dto.getTimestampSent(), entity.getTimestampSent());
        assertEquals(dto.getTimestampRead(), entity.getTimestampRead());
        // Note: Not checking sender/recipient relationships in base test
    }

    @Test
    @DisplayName("Should convert Notification entity to DTO")
    void testToDTO() {
        // Act
        NotificationDTO result = notificationConverter.toDTO(notification);

        // Assert
        assertNotNull(result);
        assertEquals(notification.getId(), result.getId());
        assertEquals(notification.getContent(), result.getContent());
        assertEquals(notification.getType(), result.getType());
        assertEquals(notification.isRead(), result.isRead());
        assertEquals(notification.getTimestampSent(), result.getTimestampSent());
        assertEquals(notification.getTimestampRead(), result.getTimestampRead());
        assertEquals(notification.getSender().getId(), result.getSenderId());
        assertEquals(notification.getRecipient().getId(), result.getRecipientId());
    }

    @Test
    @DisplayName("Should convert Notification entity to DTO with null sender")
    void testToDTOWithNullSender() {
        // Arrange
        notification.setSender(null);

        // Act
        NotificationDTO result = notificationConverter.toDTO(notification);

        // Assert
        assertNotNull(result);
        assertEquals(notification.getId(), result.getId());
        assertNull(result.getSenderId());
    }

    @Test
    @DisplayName("Should convert Notification entity to DTO with null recipient")
    void testToDTOWithNullRecipient() {
        // Arrange
        notification.setRecipient(null);

        // Act
        NotificationDTO result = notificationConverter.toDTO(notification);

        // Assert
        assertNotNull(result);
        assertEquals(notification.getId(), result.getId());
        assertNull(result.getRecipientId());
    }

    @Test
    @DisplayName("Should convert NotificationDTO to entity")
    void testToEntity() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockSender);
        lenient().when(entityManager.find(eq(User.class), eq(2L))).thenReturn(mockRecipient);

        // Act
        Notification result = notificationConverter.toEntity(notificationDTO);

        // Assert
        assertNotNull(result);
        assertEquals(notificationDTO.getContent(), result.getContent());
        assertEquals(notificationDTO.getType(), result.getType());
        assertEquals(notificationDTO.isRead(), result.isRead());
        assertEquals(notificationDTO.getTimestampSent(), result.getTimestampSent());
        assertEquals(notificationDTO.getTimestampRead(), result.getTimestampRead());
        assertEquals(mockSender, result.getSender());
        assertEquals(mockRecipient, result.getRecipient());
    }

    @Test
    @DisplayName("Should convert NotificationDTO to entity with null senderId")
    void testToEntityWithNullSenderId() {
        // Arrange
        notificationDTO.setSenderId(null);
        lenient().when(entityManager.find(eq(User.class), eq(2L))).thenReturn(mockRecipient);

        // Act
        Notification result = notificationConverter.toEntity(notificationDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getSender());
        assertEquals(mockRecipient, result.getRecipient());
    }

    @Test
    @DisplayName("Should convert NotificationDTO to entity with null recipientId")
    void testToEntityWithNullRecipientId() {
        // Arrange
        notificationDTO.setRecipientId(null);
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockSender);

        // Act
        Notification result = notificationConverter.toEntity(notificationDTO);

        // Assert
        assertNotNull(result);
        assertEquals(mockSender, result.getSender());
        assertNull(result.getRecipient());
    }

    @Test
    @DisplayName("Should convert NotificationDTO to entity with non-existent sender")
    void testToEntityWithNonExistentSender() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(null);
        lenient().when(entityManager.find(eq(User.class), eq(2L))).thenReturn(mockRecipient);

        // Act
        Notification result = notificationConverter.toEntity(notificationDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getSender());
        assertEquals(mockRecipient, result.getRecipient());
    }

    @Test
    @DisplayName("Should handle null entity in toDTO")
    void testToDTOWithNullEntity() {
        // Act & Assert
        assertNull(notificationConverter.toDTO(null));
    }

    @Test
    @DisplayName("Should handle null DTO in toEntity")
    void testToEntityWithNullDTO() {
        // Act & Assert
        assertNull(notificationConverter.toEntity(null));
    }

    @Test
    @DisplayName("Should get correct entity class")
    void testGetEntityClass() {
        // Act & Assert
        assertEquals(Notification.class, notificationConverter.getEntityClass());
    }

    @Test
    @DisplayName("Should get ID from DTO")
    void testGetIdFromDTO() {
        // Act & Assert
        assertEquals(1L, notificationConverter.getIdFromDTO(notificationDTO));
    }

    @Test
    @DisplayName("Should handle null ID in DTO")
    void testGetIdFromDTOWithNull() {
        // Arrange
        notificationDTO.setId(null);

        // Act & Assert
        assertNull(notificationConverter.getIdFromDTO(notificationDTO));
    }
}
