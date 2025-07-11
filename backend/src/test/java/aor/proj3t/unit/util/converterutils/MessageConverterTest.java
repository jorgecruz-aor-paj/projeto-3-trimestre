package aor.proj3t.unit.util.converterutils;

import aor.proj3t.dto.MessageDTO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.User;
import aor.proj3t.util.converterutils.MessageConverter;
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
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for MessageConverter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MessageConverter Tests")
public class MessageConverterTest extends BaseConverterTest<Message, MessageDTO, MessageConverter> {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private MessageConverter messageConverter;

    private Message message;
    private MessageDTO messageDTO;
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

        // Create test message
        message = new Message();
        message.setId(1L);
        message.setContent("Hello, this is a test message");
        message.setTimestampSent(Instant.now());
        message.setTimestampRead(Instant.now().plusSeconds(300));
        message.setRead(true);
        message.setSender(mockSender);
        message.setRecipient(mockRecipient);

        // Create test DTO
        messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setContent("Hello, this is a test message");
        messageDTO.setTimestampSent(Instant.now());
        messageDTO.setTimestampRead(Instant.now().plusSeconds(300));
        messageDTO.setRead(true);
        messageDTO.setSenderId(1L);
        messageDTO.setRecipientId(2L);
    }

    @Override
    protected MessageConverter getConverter() {
        return messageConverter;
    }

    @Override
    protected Message createValidEntity() {
        Message entity = new Message();
        entity.setId(1L);
        entity.setContent("Hello, this is a test message");
        entity.setTimestampSent(Instant.now());
        entity.setTimestampRead(Instant.now().plusSeconds(300));
        entity.setRead(true);
        // Don't set sender/recipient for base test
        return entity;
    }

    @Override
    protected MessageDTO createValidDTO() {
        MessageDTO dto = new MessageDTO();
        dto.setId(1L);
        dto.setContent("Hello, this is a test message");
        dto.setTimestampSent(Instant.now());
        dto.setTimestampRead(Instant.now().plusSeconds(300));
        dto.setRead(true);
        // Don't set senderId/recipientId for base test
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(Message entity, MessageDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getContent(), dto.getContent());
        assertEquals(entity.getTimestampSent(), dto.getTimestampSent());
        assertEquals(entity.getTimestampRead(), dto.getTimestampRead());
        assertEquals(entity.isRead(), dto.isRead());
        // Note: Not checking sender/recipient relationships in base test
    }

    @Override
    protected void assertDTOEqualsEntity(MessageDTO dto, Message entity) {
        // Don't check ID as it comes from database, not DTO
        assertEquals(dto.getContent(), entity.getContent());
        assertEquals(dto.getTimestampSent(), entity.getTimestampSent());
        assertEquals(dto.getTimestampRead(), entity.getTimestampRead());
        assertEquals(dto.isRead(), entity.isRead());
        // Note: Not checking sender/recipient relationships in base test
    }

    @Test
    @DisplayName("Should convert Message entity to DTO")
    void testToDTO() {
        // Act
        MessageDTO result = messageConverter.toDTO(message);

        // Assert
        assertNotNull(result);
        assertEquals(message.getId(), result.getId());
        assertEquals(message.getContent(), result.getContent());
        assertEquals(message.getTimestampSent(), result.getTimestampSent());
        assertEquals(message.getTimestampRead(), result.getTimestampRead());
        assertEquals(message.isRead(), result.isRead());
        assertEquals(message.getSender().getId(), result.getSenderId());
        assertEquals(message.getRecipient().getId(), result.getRecipientId());
    }

    @Test
    @DisplayName("Should convert Message entity to DTO with null sender")
    void testToDTOWithNullSender() {
        // Arrange
        message.setSender(null);

        // Act
        MessageDTO result = messageConverter.toDTO(message);

        // Assert
        assertNotNull(result);
        assertEquals(message.getId(), result.getId());
        assertNull(result.getSenderId());
    }

    @Test
    @DisplayName("Should convert Message entity to DTO with null recipient")
    void testToDTOWithNullRecipient() {
        // Arrange
        message.setRecipient(null);

        // Act
        MessageDTO result = messageConverter.toDTO(message);

        // Assert
        assertNotNull(result);
        assertEquals(message.getId(), result.getId());
        assertNull(result.getRecipientId());
    }

    @Test
    @DisplayName("Should convert MessageDTO to entity")
    void testToEntity() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockSender);
        lenient().when(entityManager.find(eq(User.class), eq(2L))).thenReturn(mockRecipient);

        // Act
        Message result = messageConverter.toEntity(messageDTO);

        // Assert
        assertNotNull(result);
        assertEquals(messageDTO.getContent(), result.getContent());
        assertEquals(messageDTO.getTimestampSent(), result.getTimestampSent());
        assertEquals(messageDTO.getTimestampRead(), result.getTimestampRead());
        assertEquals(messageDTO.isRead(), result.isRead());
        assertEquals(mockSender, result.getSender());
        assertEquals(mockRecipient, result.getRecipient());
    }

    @Test
    @DisplayName("Should convert MessageDTO to entity with null senderId")
    void testToEntityWithNullSenderId() {
        // Arrange
        messageDTO.setSenderId(null);
        lenient().when(entityManager.find(eq(User.class), eq(2L))).thenReturn(mockRecipient);

        // Act
        Message result = messageConverter.toEntity(messageDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getSender());
        assertEquals(mockRecipient, result.getRecipient());
    }

    @Test
    @DisplayName("Should convert MessageDTO to entity with null recipientId")
    void testToEntityWithNullRecipientId() {
        // Arrange
        messageDTO.setRecipientId(null);
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockSender);

        // Act
        Message result = messageConverter.toEntity(messageDTO);

        // Assert
        assertNotNull(result);
        assertEquals(mockSender, result.getSender());
        assertNull(result.getRecipient());
    }

    @Test
    @DisplayName("Should convert MessageDTO to entity with non-existent sender")
    void testToEntityWithNonExistentSender() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(null);
        lenient().when(entityManager.find(eq(User.class), eq(2L))).thenReturn(mockRecipient);

        // Act
        Message result = messageConverter.toEntity(messageDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getSender());
        assertEquals(mockRecipient, result.getRecipient());
    }

    @Test
    @DisplayName("Should handle null entity in toDTO")
    void testToDTOWithNullEntity() {
        // Act & Assert
        assertNull(messageConverter.toDTO(null));
    }

    @Test
    @DisplayName("Should handle null DTO in toEntity")
    void testToEntityWithNullDTO() {
        // Act & Assert
        assertNull(messageConverter.toEntity(null));
    }

    @Test
    @DisplayName("Should get correct entity class")
    void testGetEntityClass() {
        // Act & Assert
        assertEquals(Message.class, messageConverter.getEntityClass());
    }

    @Test
    @DisplayName("Should get ID from DTO")
    void testGetIdFromDTO() {
        // Act & Assert
        assertEquals(1L, messageConverter.getIdFromDTO(messageDTO));
    }

    @Test
    @DisplayName("Should handle null ID in DTO")
    void testGetIdFromDTOWithNull() {
        // Arrange
        messageDTO.setId(null);

        // Act & Assert
        assertNull(messageConverter.getIdFromDTO(messageDTO));
    }
}
