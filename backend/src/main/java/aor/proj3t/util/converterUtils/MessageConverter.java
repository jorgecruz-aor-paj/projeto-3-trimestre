package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.MessageDTO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageConverter extends AbstractConverter<Message, MessageDTO, Long> {

    @Override
    protected Class<Message> getEntityClass() {
        return Message.class;
    }

    @Override
    protected Message createEntityInstance() {
        return new Message();
    }

    @Override
    protected MessageDTO createDTOInstance() {
        return new MessageDTO();
    }

    @Override
    protected Long getIdFromDTO(MessageDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(Message message, MessageDTO dto) {
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
    protected void mapDTOToEntity(MessageDTO dto, Message message) {
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
