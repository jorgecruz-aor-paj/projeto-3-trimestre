package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.Message;
import aor.proj3t.entities.User;

import java.util.List;

public interface MessageDAO extends BasicDAO<Message, Long> {

    /**
     * Finds all messages sent by a specific user.
     *
     * @param senderId The ID of the sender.
     * @return A list of messages sent by the specified user.
     */
    List<Message> findBySenderId(Long senderId);

    /**
     * Finds all messages received by a specific user.
     *
     * @param recipientId The ID of the recipient.
     * @return A list of messages received by the specified user.
     */
    List<Message> findByRecipientId(Long recipientId);

    /**
     * Finds all messages exchanged between a sender and recipient.
     *
     * @param senderId   The ID of the sender.
     * @param recipientId The ID of the recipient.
     * @return List of messages exchanged between the sender and recipient.
     */
    List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    /**
     * Finds all unique users that a given user has exchanged messages with.
     *
     * @param userId The ID of the user.
     * @return A list of unique users who have interacted with the user.
     */
    List<User> findMessagedUsers(Long userId);

    /**
     * Counts the number of unread messages of a user.
     *
     * @param recipientId The ID of the user.
     * @return A count of unread messages.
     */
    Integer findCountUnreadMessagesByRecipientId(Long recipientId);

    /**
     * Marks a message has read.
     *
     * @param messageId The ID of the message.
     * @return True for success and False for unsuccessful marking.
     */
    Boolean markMessageAsRead(Long messageId);

}
