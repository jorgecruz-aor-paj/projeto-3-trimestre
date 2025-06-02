package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.Message;

import java.time.LocalDateTime;
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

}

