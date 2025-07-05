package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.MessageDTO;
import aor.proj3t.entities.Message;
import aor.proj3t.service.interfaces.MessageService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the MessageService interface
 */
@ApplicationScoped
public class MessageServiceImpl extends BaseService implements MessageService {

    private static final Logger logger = LogManager.getLogger(MessageServiceImpl.class);

    @Inject
    private DAOFactory daoFactory;

    @Inject
    private ConverterFactory converterFactory;

    @Override
    @Transactional
    public Message createMessage(Message message) {
        message.setTimestampSent(Instant.now());
        message.setRead(false);
        daoFactory.forMessage().create(message);
        logger.info("Message created with ID: {}", message.getId());
        return message;
    }

    @Override
    public MessageDTO getMessage(Long messageId) {
        Optional<Message> messageOpt = daoFactory.forMessage().findById(messageId);
        Message message = getEntity(messageOpt, messageId, "Message");
        return converterFactory.forMessage().toDTO(message);
    }

    @Override
    @Transactional
    public Boolean markAsRead(Long messageId) {
        return daoFactory.forMessage().markMessageAsRead(messageId);
    }

    @Override
    public List<MessageDTO> getMessagesBetweenUsers(Long senderId, Long recipientId) {
        // Get messages in both directions
        List<Message> sentMessages = daoFactory.forMessage().findBySenderIdAndRecipientId(senderId, recipientId);
        List<Message> receivedMessages = daoFactory.forMessage().findBySenderIdAndRecipientId(recipientId, senderId);

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);

        // Sort by timestamp
        allMessages.sort((a, b) -> a.getTimestampSent().compareTo(b.getTimestampSent()));

        return converterFactory.forMessage().toDTOList(allMessages);
    }

    @Override
    public List<MessageDTO> getUserMessages(Long userId) {
        List<Message> sentMessages = daoFactory.forMessage().findBySenderId(userId);
        List<Message> receivedMessages = daoFactory.forMessage().findByRecipientId(userId);

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);

        return converterFactory.forMessage().toDTOList(allMessages);
    }

    @Override
    @Transactional
    public Boolean deleteMessage(Long messageId) {
        Optional<Message> messageOpt = daoFactory.forMessage().findById(messageId);
        if (!messageOpt.isPresent()) {
            logger.warn("Message with ID: {} not found for deletion", messageId);
            return false;
        }

        boolean success = daoFactory.forMessage().deleteById(messageId);
        if (success) {
            logger.info("Message with ID: {} deleted", messageId);
        } else {
            logger.error("Failed to delete message with ID: {}", messageId);
        }
        return success;
    }

    @Override
    public List<aor.proj3t.entities.User> getMessagedUsers(Long userId) {
        return daoFactory.forMessage().findMessagedUsers(userId);
    }

    @Override
    public Integer getUnreadMessageCount(Long userId) {
        return daoFactory.forMessage().findCountUnreadMessagesByRecipientId(userId);
    }
}
