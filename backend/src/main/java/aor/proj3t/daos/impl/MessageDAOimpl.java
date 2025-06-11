package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.List;

import aor.proj3t.daos.interfaces.MessageDAO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.User;
import jakarta.ejb.Stateless;

@Stateless
public class MessageDAOimpl extends AbstractDAO<Message, Long> implements MessageDAO {

    public MessageDAOimpl() {
        super(Message.class);
    }

    @Override
    public List<Message> findBySenderId(Long senderId) {

        return em.createQuery("SELECT m FROM Message m WHERE m.sender.id = :senderId", Message.class)
                .setParameter("senderId", senderId)
                .getResultList();
    }

    @Override
    public List<Message> findByRecipientId(Long recipientId) {

        return em.createQuery(" SELECT m FROM Message m WHERE m.recipient.id = :recipientId", Message.class)
                .setParameter("recipientId", recipientId)
                .getResultList();
    }

    @Override
    public List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId) {

        return em
                .createQuery("SELECT m FROM Message m WHERE m.sender.id = :senderId AND m.recipient.id = :recipientId",
                        Message.class)
                .setParameter("senderId", senderId)
                .setParameter("recipientId", recipientId)
                .getResultList();
    }

    @Override
    public List<User> findMessagedUsers(Long userId) {

        return em.createQuery(
                "SELECT DISTINCT u FROM User u WHERE u.id IN (" +
                        "SELECT m.sender.id FROM Message m WHERE m.recipient.id = :userId UNION " +
                        "SELECT m.recipient.id FROM Message m WHERE m.sender.id = :userId)",
                User.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Integer findCountUnreadMessagesByRecipientId(Long recipientId) {

        return em
                .createQuery("SELECT COUNT(m) FROM Message m WHERE m.recipient.id = :recipientId AND m.isRead = FALSE",
                        Long.class)
                .setParameter("recipientId", recipientId)
                .getSingleResult()
                .intValue();
    }

    @Override
    public List<Message> findUnreadMessagesByRecipientId(Long recipientId) {

        return em
                .createQuery("SELECT m FROM Message m WHERE m.recipient.id = :recipientId AND m.isRead = FALSE",
                        Message.class)
                .setParameter("recipientId", recipientId)
                .getResultList();
    }

    @Override
    public Boolean markMessageAsRead(Long messageId) {

        int updatedCount = em
                .createQuery(
                        "UPDATE Message m SET m.isRead = TRUE, m.timestampRead = :timestamp WHERE m.id = :messageId")
                .setParameter("timestamp", Instant.now())
                .setParameter("messageId", messageId)
                .executeUpdate();

        return updatedCount > 0;
    }
}
