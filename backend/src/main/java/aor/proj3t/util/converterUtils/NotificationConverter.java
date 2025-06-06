package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationConverter extends AbstractConverter<Notification, NotificationDTO, Long> {
    @Override
    protected Class<Notification> getEntityClass() {
        return Notification.class;
    }

    @Override
    protected Notification createEntityInstance() {
        return new Notification();
    }

    @Override
    protected NotificationDTO createDTOInstance() {
        return new NotificationDTO();
    }

    @Override
    protected Long getIdFromDTO(NotificationDTO dto) {
        return dto.getId();
    }

    @Override
    protected void mapEntityToDTO(Notification notification, NotificationDTO dto) {
        dto.setId(notification.getId());
        dto.setContent(notification.getContent());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setTimestampSent(notification.getTimestampSent());
        dto.setTimestampRead(notification.getTimestampRead());
        dto.setSenderId(notification.getSender() != null ? notification.getSender().getId() : null);
        dto.setRecipientId(notification.getRecipient() != null ? notification.getRecipient().getId() : null);
    }

    @Override
    protected void mapDTOToEntity(NotificationDTO dto, Notification notification) {
        notification.setContent(dto.getContent());
        notification.setType(dto.getType());
        notification.setRead(dto.isRead());
        notification.setTimestampSent(dto.getTimestampSent());
        notification.setTimestampRead(dto.getTimestampRead());

        // Handle relationships
        User sender = findSafely(User.class, dto.getSenderId());
        notification.setSender(sender);

        User recipient = findSafely(User.class, dto.getRecipientId());
        notification.setRecipient(recipient);
    }
}
