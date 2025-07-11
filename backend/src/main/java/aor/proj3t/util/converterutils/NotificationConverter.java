package aor.proj3t.util.converterutils;

import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Converter for Notification entities and DTOs.
 * 
 * This class handles the bidirectional conversion between {@link Notification}
 * entities and
 * {@link NotificationDTO} data transfer objects. Notifications represent system
 * messages
 * to users with different types, read status tracking, and timestamps for
 * delivery and reading.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * 
 * @version 1.0
 */
@ApplicationScoped
public class NotificationConverter extends AbstractConverter<Notification, NotificationDTO, Long> {
    @Override
    public Class<Notification> getEntityClass() {
        return Notification.class;
    }

    @Override
    public Notification createEntityInstance() {
        return new Notification();
    }

    @Override
    public NotificationDTO createDTOInstance() {
        return new NotificationDTO();
    }

    @Override
    public Long getIdFromDTO(NotificationDTO dto) {
        return dto.getId();
    }

    @Override
    public void mapEntityToDTO(Notification notification, NotificationDTO dto) {
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
    public void mapDTOToEntity(NotificationDTO dto, Notification notification) {
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
