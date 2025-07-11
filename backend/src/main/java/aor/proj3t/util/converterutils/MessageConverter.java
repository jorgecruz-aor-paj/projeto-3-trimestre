package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MessageDTO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Converter for Message entities and DTOs.
 * 
 * This class handles the bidirectional conversion between {@link Message}
 * entities and
 * {@link MessageDTO} data transfer objects. It manages user message data
 * including
 * sender and recipient relationships, message content, and read status
 * tracking.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class MessageConverter extends AbstractConverter<Message, MessageDTO, Long> {

    @Override
    public Class<Message> getEntityClass() {
        return Message.class;
    }

    @Override
    public Message createEntityInstance() {
        return new Message();
    }

    @Override
    public MessageDTO createDTOInstance() {
        return new MessageDTO();
    }

    @Override
    public Long getIdFromDTO(MessageDTO dto) {
        return dto.getId();
    }

    @Override
    public void mapEntityToDTO(Message message, MessageDTO dto) {
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setTimestampSent(message.getTimestampSent());
        dto.setTimestampRead(message.getTimestampRead());
        dto.setRead(message.isRead());

        if (message.getSender() != null) {
            dto.setSenderId(message.getSender().getId());
        }

        if (message.getRecipient() != null) {
            dto.setRecipientId(message.getRecipient().getId());
        }
    }

    @Override
    public void mapDTOToEntity(MessageDTO dto, Message message) {
        message.setContent(dto.getContent());
        message.setTimestampSent(dto.getTimestampSent());
        message.setTimestampRead(dto.getTimestampRead());
        message.setRead(dto.isRead());

        // Handle relationships using the findSafely method
        User sender = findSafely(User.class, dto.getSenderId());
        message.setSender(sender);

        User recipient = findSafely(User.class, dto.getRecipientId());
        message.setRecipient(recipient);
    }
}
