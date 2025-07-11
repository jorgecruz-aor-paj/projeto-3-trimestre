package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.Message;
import aor.proj3t.entities.User;

/**
 * Data Access Object interface for Message entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide comprehensive operations
 * for managing internal messaging system within the application. Messages
 * facilitate communication between users in the system.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Querying messages by sender and recipient</li>
 * <li>Finding conversation participants</li>
 * <li>Counting unread messages for notifications</li>
 * <li>Message status management (read/unread)</li>
 * <li>Conversation history tracking</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see Message
 * @see User
 * @see BasicDAO
 */
public interface MessageDAO extends BasicDAO<Message, Long> {

    /**
     * Retrieves all messages sent by a specific user.
     * <p>
     * This method finds all messages where the specified user is the sender.
     * Useful for displaying user's sent message history and outbox functionality.
     * </p>
     *
     * @param senderId The unique identifier of the sender. Must not be null.
     * @return A {@link List} of messages sent by the specified user,
     *         or an empty list if no messages are found
     * @throws IllegalArgumentException               if senderId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<Message> findBySenderId(Long senderId);

    /**
     * Retrieves all messages received by a specific user.
     * <p>
     * This method finds all messages where the specified user is the recipient.
     * Essential for displaying user's inbox and message history.
     * </p>
     *
     * @param recipientId The unique identifier of the recipient. Must not be null.
     * @return A {@link List} of messages received by the specified user,
     *         or an empty list if no messages are found
     * @throws IllegalArgumentException               if recipientId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<Message> findByRecipientId(Long recipientId);

    /**
     * Retrieves all messages exchanged between two specific users.
     * <p>
     * This method finds all messages where one user is the sender and the other
     * is the recipient, regardless of direction. Useful for displaying complete
     * conversation history between two users.
     * </p>
     *
     * @param senderId    The unique identifier of the sender. Must not be null.
     * @param recipientId The unique identifier of the recipient. Must not be null.
     * @return A {@link List} of messages exchanged between the sender and
     *         recipient,
     *         or an empty list if no messages are found
     * @throws IllegalArgumentException               if senderId or recipientId is
     *                                                null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    /**
     * Retrieves all unique users that have exchanged messages with a given user.
     * <p>
     * This method finds all users who have either sent messages to or received
     * messages from the specified user. Essential for displaying conversation
     * participants and contact lists.
     * </p>
     *
     * @param userId The unique identifier of the user. Must not be null.
     * @return A {@link List} of unique users who have interacted with the specified
     *         user,
     *         or an empty list if no interactions are found
     * @throws IllegalArgumentException               if userId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<User> findMessagedUsers(Long userId);

    /**
     * Counts the total number of unread messages for a specific user.
     * <p>
     * This method provides a count of all messages received by the user that
     * have not been marked as read. Essential for notification badges and
     * message alerts in the user interface.
     * </p>
     *
     * @param recipientId The unique identifier of the recipient. Must not be null.
     * @return The count of unread messages, or 0 if no unread messages exist
     * @throws IllegalArgumentException               if recipientId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Integer findCountUnreadMessagesByRecipientId(Long recipientId);

    /**
     * Counts unread messages between two specific users.
     * <p>
     * This method provides a count of unread messages from a specific sender
     * to a specific recipient. Useful for displaying conversation-specific
     * unread message counts.
     * </p>
     *
     * @param recipientId The unique identifier of the recipient. Must not be null.
     * @param senderId    The unique identifier of the sender. Must not be null.
     * @return The count of unread messages from sender to recipient, or 0 if none
     *         exist
     * @throws IllegalArgumentException               if recipientId or senderId is
     *                                                null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Integer findCountUnreadMessagesBetweenUsers(Long recipientId, Long senderId);

    /**
     * Marks a specific message as read.
     * <p>
     * This method updates the read status of a message to indicate that it has
     * been viewed by the recipient. This affects unread message counts and
     * notification states.
     * </p>
     *
     * @param messageId The unique identifier of the message to mark as read. Must
     *                  not be null.
     * @return {@code true} if the message was successfully marked as read,
     *         {@code false} if the operation failed or the message was not found
     * @throws IllegalArgumentException               if messageId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Boolean markMessageAsRead(Long messageId);

}
