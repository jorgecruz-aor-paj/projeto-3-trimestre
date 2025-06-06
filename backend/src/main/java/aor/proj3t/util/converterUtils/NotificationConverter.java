package aor.proj3t.util.converterUtils;

import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class NotificationConverter implements Converter<Notification, NotificationDTO> {

    @Inject
    private EntityManager em;

    @Override
    public NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        return new NotificationDTO(
                notification.getId(),
                notification.getContent(),
                notification.getType(),
                notification.isRead(),
                notification.getTimestampSent(),
                notification.getTimestampRead(),
                notification.getSender() != null ? notification.getSender().getId() : null,
                notification.getRecipient() != null ? notification.getRecipient().getId() : null);
    }

    @Override
    public Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification notification;
        if (dto.getId() != null && dto.getId() != 0) {
            notification = em.find(Notification.class, dto.getId());
            if (notification == null) {
                notification = new Notification();
            }
        } else {
            notification = new Notification();
        }

        notification.setContent(dto.getContent());
        notification.setType(dto.getType());
        notification.setRead(dto.isRead());
        notification.setTimestampSent(dto.getTimestampSent());
        notification.setTimestampRead(dto.getTimestampRead());

        // Handle relationships
        if (dto.getSenderId() != null) {
            User sender = em.find(User.class, dto.getSenderId());
            notification.setSender(sender);
        }

        if (dto.getRecipientId() != null) {
            User recipient = em.find(User.class, dto.getRecipientId());
            notification.setRecipient(recipient);
        }

        return notification;
    }
}
