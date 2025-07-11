package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.dto.MessageDTO;
import aor.proj3t.entities.Message;

/**
 * Service interface for messaging operations.
 * This interface defines the contract for comprehensive messaging functionality
 * including private messages between users, conversation threads, and
 * notification integration.
 * 
 * <p>
 * Key messaging services include:
 * <ul>
 * <li><b>Message Creation:</b> Creating and sending messages between users</li>
 * <li><b>Conversation Management:</b> Managing message threads and conversation
 * history</li>
 * <li><b>Read Status Tracking:</b> Tracking message read states and
 * timestamps</li>
 * <li><b>Notification Integration:</b> Automatic notification creation for new
 * messages</li>
 * <li><b>User Communication:</b> Secure messaging with proper authorization
 * controls</li>
 * <li><b>Message History:</b> Retrieving conversation history and message
 * threads</li>
 * </ul>
 * 
 * <p>
 * This service provides secure internal messaging capabilities with proper
 * authorization
 * checks and notification integration to ensure users are promptly informed of
 * new messages.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
public interface MessageService {

    /**
     * Creates a new message
     * 
     * @param message The message entity to create
     * @return The created message
     */
    Message createMessage(Message message);

    /**
     * Retrieves a message by its ID
     * 
     * @param messageId The ID of the message to retrieve
     * @return The message DTO
     */
    MessageDTO getMessage(Long messageId);

    /**
     * Updates a message's read status
     * 
     * @param messageId The ID of the message to mark as read
     * @return true if successful, false otherwise
     */
    Boolean markAsRead(Long messageId);

    /**
     * Retrieves all messages between two users
     * 
     * @param senderId    The ID of the sender
     * @param recipientId The ID of the recipient
     * @return List of message DTOs
     */
    List<MessageDTO> getMessagesBetweenUsers(Long senderId, Long recipientId);

    /**
     * Retrieves all messages for a user (both sent and received)
     * 
     * @param userId The user ID
     * @return List of message DTOs
     */
    List<MessageDTO> getUserMessages(Long userId);

    /**
     * Gets all users that have exchanged messages with the specified user
     * 
     * @param userId The user ID
     * @return List of users who have messaged with this user
     */
    List<aor.proj3t.entities.User> getMessagedUsers(Long userId);

    /**
     * Gets the count of unread messages for a user
     * 
     * @param userId The user ID
     * @return Number of unread messages
     */
    Integer getUnreadMessageCount(Long userId);

    /**
     * Gets the count of unread messages between two users
     * 
     * @param recipientId The user ID who received the messages
     * @param senderId    The user ID who sent the messages
     * @return Number of unread messages from sender to recipient
     */
    Integer getUnreadMessageCountBetweenUsers(Long recipientId, Long senderId);

}
