package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.List;

import aor.proj3t.daos.interfaces.NotificationDAO;
import aor.proj3t.entities.Notification;
import jakarta.ejb.Stateless;

@Stateless
public class NotificationDAOimpl extends AbstractDAO<Notification, Long> implements NotificationDAO {

        @Override
        protected Class<Notification> getEntityClass() {
                return Notification.class;
        }

        @Override
        public List<Notification> findBySenderId(Long senderId) {

                return em.createQuery(
                                "SELECT n FROM Notification n WHERE n.sender.id = :senderId ORDER BY n.timestampSent DESC",
                                Notification.class)
                                .setParameter("senderId", senderId)
                                .getResultList();
        }

        @Override
        public List<Notification> findByRecipientId(Long recipientId) {

                return em.createQuery(
                                "SELECT n FROM Notification n WHERE n.recipient.id = :recipientId ORDER BY n.timestampSent DESC",
                                Notification.class)
                                .setParameter("recipientId", recipientId)
                                .getResultList();
        }

        @Override
        public List<Notification> findByRecipientIdPaginated(Long recipientId, int offset, int limit) {
                return em.createQuery(
                                "SELECT n FROM Notification n WHERE n.recipient.id = :recipientId ORDER BY n.timestampSent DESC",
                                Notification.class)
                                .setParameter("recipientId", recipientId)
                                .setFirstResult(offset)
                                .setMaxResults(limit)
                                .getResultList();
        }

        @Override
        public Long countByRecipientId(Long recipientId) {
                return em.createQuery("SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :recipientId",
                                Long.class)
                                .setParameter("recipientId", recipientId)
                                .getSingleResult();
        }

        @Override
        public Long countUnreadNotificationsByRecipientId(Long recipientId) {

                return em
                                .createQuery(
                                                "SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :recipientId AND n.isRead = FALSE",
                                                Long.class)
                                .setParameter("recipientId", recipientId)
                                .getSingleResult();
        }

        @Override
        public List<Notification> findUnreadNotificationsByRecipientId(Long recipientId) {

                return em
                                .createQuery("SELECT n FROM Notification n WHERE n.recipient.id = :recipientId AND n.isRead = FALSE ORDER BY n.timestampSent DESC",
                                                Notification.class)
                                .setParameter("recipientId", recipientId)
                                .getResultList();
        }

        @Override
        public Boolean markNotificationAsRead(Long notificationId) {

                int updatedCount = em.createQuery(
                                "UPDATE Notification n  SET n.isRead = TRUE, n.timestampRead = :timestamp WHERE n.id = :notificationId")
                                .setParameter("timestamp", Instant.now())
                                .setParameter("notificationId", notificationId)
                                .executeUpdate();

                return updatedCount > 0; // True if the update was successful
        }

        @Override
        public Integer markAllNotificationsAsRead(Long recipientId) {
                int updatedCount = em.createQuery(
                                "UPDATE Notification n SET n.isRead = TRUE, n.timestampRead = :timestamp WHERE n.recipient.id = :recipientId AND n.isRead = FALSE")
                                .setParameter("timestamp", Instant.now())
                                .setParameter("recipientId", recipientId)
                                .executeUpdate();

                return updatedCount; // Number of notifications marked as read
        }
}
