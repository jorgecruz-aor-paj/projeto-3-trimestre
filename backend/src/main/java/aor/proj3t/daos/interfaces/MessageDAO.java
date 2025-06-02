package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.Message;

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

}
