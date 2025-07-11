package aor.proj3t.unit.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.MessageDAO;
import aor.proj3t.dto.MessageDTO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.impl.MessageServiceImpl;
import aor.proj3t.service.interfaces.NotificationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.MessageConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private ConverterFactory converterFactory;

    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private MessageDAO messageDAO;

    @Mock
    private MessageConverter messageConverter;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Captor
    private ArgumentCaptor<Message> messageCaptor;

    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;

    // Test data
    private Message message;
    private MessageDTO messageDTO;
    private User sender;
    private User recipient;
    private final Long MESSAGE_ID = 1L;
    private final Long SENDER_ID = 1L;
    private final Long RECIPIENT_ID = 2L;

    @BeforeEach
    void setUp() {
        // Setup common test data
        sender = new User();
        sender.setId(SENDER_ID);
        sender.setFirstName("John");
        sender.setLastName("Doe");

        recipient = new User();
        recipient.setId(RECIPIENT_ID);
        recipient.setFirstName("Jane");
        recipient.setLastName("Smith");

        message = new Message();
        message.setId(MESSAGE_ID);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent("Test message");
        message.setTimestampSent(Instant.now());

        messageDTO = new MessageDTO();
        messageDTO.setId(MESSAGE_ID);
        messageDTO.setContent("Test message");

        // Setup common mocks
        lenient().when(daoFactory.forMessage()).thenReturn(messageDAO);
        lenient().when(converterFactory.forMessage()).thenReturn(messageConverter);
        lenient().when(serviceFactory.forNotification()).thenReturn(notificationService);
    }

    @Test
    @DisplayName("createMessage should create message and notification")
    void createMessage_ShouldCreateMessageAndNotification() {
        // Given
        when(messageDAO.create(any(Message.class))).thenReturn(message);

        // When
        Message result = messageService.createMessage(message);

        // Then
        verify(messageDAO).create(messageCaptor.capture());
        Message capturedMessage = messageCaptor.getValue();
        
        assertNotNull(result);
        assertNotNull(capturedMessage.getTimestampSent());
        assertFalse(capturedMessage.isRead());
        
        verify(notificationService).createNotification(notificationCaptor.capture());
        Notification notification = notificationCaptor.getValue();
        assertEquals(NotificationType.MESSAGE, notification.getType());
        assertEquals(sender, notification.getSender());
        assertEquals(recipient, notification.getRecipient());
        assertTrue(notification.getContent().contains(sender.getFirstName()));
        assertTrue(notification.getContent().contains(sender.getLastName()));
    }

    @Test
    @DisplayName("getMessage should return message DTO")
    void getMessage_ShouldReturnMessageDTO() {
        // Given
        when(messageDAO.findById(MESSAGE_ID)).thenReturn(Optional.of(message));
        when(messageConverter.toDTO(message)).thenReturn(messageDTO);

        // When
        MessageDTO result = messageService.getMessage(MESSAGE_ID);

        // Then
        assertNotNull(result);
        assertEquals(messageDTO, result);
        verify(messageDAO).findById(MESSAGE_ID);
        verify(messageConverter).toDTO(message);
    }

    @Test
    @DisplayName("getMessage should throw NotFoundException when message not found")
    void getMessage_ShouldThrowNotFoundException_WhenMessageNotFound() {
        // Given
        when(messageDAO.findById(MESSAGE_ID)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class, () -> messageService.getMessage(MESSAGE_ID));
        verify(messageDAO).findById(MESSAGE_ID);
        verifyNoInteractions(messageConverter);
    }

    @Test
    @DisplayName("markAsRead should mark message as read")
    void markAsRead_ShouldMarkMessageAsRead() {
        // Given
        when(messageDAO.markMessageAsRead(MESSAGE_ID)).thenReturn(true);

        // When
        Boolean result = messageService.markAsRead(MESSAGE_ID);

        // Then
        assertTrue(result);
        verify(messageDAO).markMessageAsRead(MESSAGE_ID);
    }

    @Test
    @DisplayName("getMessagesBetweenUsers should return messages between users")
    void getMessagesBetweenUsers_ShouldReturnMessagesBetweenUsers() {
        // Given
        List<Message> sentMessages = Arrays.asList(message);
        Message receivedMessage = new Message();
        receivedMessage.setTimestampSent(Instant.now());
        List<Message> receivedMessages = Arrays.asList(receivedMessage);
        List<MessageDTO> expectedDTOs = Arrays.asList(messageDTO);

        when(messageDAO.findBySenderIdAndRecipientId(SENDER_ID, RECIPIENT_ID)).thenReturn(sentMessages);
        when(messageDAO.findBySenderIdAndRecipientId(RECIPIENT_ID, SENDER_ID)).thenReturn(receivedMessages);
        when(messageConverter.toDTOList(any())).thenReturn(expectedDTOs);

        // When
        List<MessageDTO> result = messageService.getMessagesBetweenUsers(SENDER_ID, RECIPIENT_ID);

        // Then
        assertNotNull(result);
        assertEquals(expectedDTOs, result);
        verify(messageDAO).findBySenderIdAndRecipientId(SENDER_ID, RECIPIENT_ID);
        verify(messageDAO).findBySenderIdAndRecipientId(RECIPIENT_ID, SENDER_ID);
        verify(messageConverter).toDTOList(any());
    }

    @Test
    @DisplayName("getUserMessages should return all user messages")
    void getUserMessages_ShouldReturnAllUserMessages() {
        // Given
        List<Message> sentMessages = Arrays.asList(message);
        Message receivedMessage = new Message();
        receivedMessage.setTimestampSent(Instant.now());
        List<Message> receivedMessages = Arrays.asList(receivedMessage);
        List<MessageDTO> expectedDTOs = Arrays.asList(messageDTO);

        when(messageDAO.findBySenderId(SENDER_ID)).thenReturn(sentMessages);
        when(messageDAO.findByRecipientId(SENDER_ID)).thenReturn(receivedMessages);
        when(messageConverter.toDTOList(any())).thenReturn(expectedDTOs);

        // When
        List<MessageDTO> result = messageService.getUserMessages(SENDER_ID);

        // Then
        assertNotNull(result);
        assertEquals(expectedDTOs, result);
        verify(messageDAO).findBySenderId(SENDER_ID);
        verify(messageDAO).findByRecipientId(SENDER_ID);
        verify(messageConverter).toDTOList(any());
    }

    @Test
    @DisplayName("getMessagedUsers should return users with message history")
    void getMessagedUsers_ShouldReturnUsersWithMessageHistory() {
        // Given
        List<User> expectedUsers = Arrays.asList(recipient);
        when(messageDAO.findMessagedUsers(SENDER_ID)).thenReturn(expectedUsers);

        // When
        List<User> result = messageService.getMessagedUsers(SENDER_ID);

        // Then
        assertNotNull(result);
        assertEquals(expectedUsers, result);
        verify(messageDAO).findMessagedUsers(SENDER_ID);
    }

    @Test
    @DisplayName("getUnreadMessageCount should return count of unread messages")
    void getUnreadMessageCount_ShouldReturnUnreadCount() {
        // Given
        Integer expectedCount = 5;
        when(messageDAO.findCountUnreadMessagesByRecipientId(RECIPIENT_ID)).thenReturn(expectedCount);

        // When
        Integer result = messageService.getUnreadMessageCount(RECIPIENT_ID);

        // Then
        assertEquals(expectedCount, result);
        verify(messageDAO).findCountUnreadMessagesByRecipientId(RECIPIENT_ID);
    }
}
