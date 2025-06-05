package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.MessageDTO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class MessageConverter implements Converter<Message, MessageDTO> {

    @Inject
    private EntityManager em;

    @Override
    public MessageDTO toDTO(Message message) {
        if (message == null) {
            return null;
        }

        return new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getTimestampSent(),
                message.getTimestampRead(),
                message.isRead(),
                message.getSender() != null ? message.getSender().getId() : null,
                message.getRecipient() != null ? message.getRecipient().getId() : null);
    }

    @Override
    public Message toEntity(MessageDTO dto) {
        if (dto == null) {
            return null;
        }

        Message message;
        if (dto.getId() != null && dto.getId() != 0) {
            message = em.find(Message.class, dto.getId());
            if (message == null) {
                message = new Message();
            }
        } else {
            message = new Message();
        }

        message.setContent(dto.getContent());
        message.setTimestampSent(dto.getTimestampSent());
        message.setTimestampRead(dto.getTimestampRead());
        message.setRead(dto.isRead());

        // Handle relationships
        if (dto.getSenderId() != null) {
            User sender = em.find(User.class, dto.getSenderId());
            message.setSender(sender);
        }

        if (dto.getRecipientId() != null) {
            User recipient = em.find(User.class, dto.getRecipientId());
            message.setRecipient(recipient);
        }

        return message;
    }
}
