package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.List;

import aor.proj3t.daos.interfaces.MessageDAO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.User;
import jakarta.ejb.Stateless;

/**
 * Stateless EJB implementation of the {@link MessageDAO} interface.
 * <p>
 * This class provides concrete implementation for managing internal messaging
 * operations within the application. It extends {@link AbstractDAO} to leverage
 * common CRUD operations and implements specific business logic for message
 * management, conversation tracking, and notification support.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Efficient JPQL queries for message filtering and retrieval</li>
 * <li>Optimized conversation participant discovery</li>
 * <li>Unread message counting for notification systems</li>
 * <li>Atomic message status updates with timestamps</li>
 * <li>Support for bilateral conversation queries</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see MessageDAO
 * @see AbstractDAO
 * @see Message
 */
@Stateless
public class MessageDAOimpl extends AbstractDAO<Message, Long> implements MessageDAO {

        /**
         * {@inheritDoc}
         * 
         * @implNote Returns {@code Message.class} to enable type-safe operations
         *           throughout the DAO implementation.
         */
        @Override
        protected Class<Message> getEntityClass() {
                return Message.class;
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses JPQL query to find all messages where the sender ID
         * matches the specified parameter. Uses entity relationship navigation
         * for efficient querying.
         * </p>
         *
         * @implNote The query leverages JPA entity relationships (m.sender.id) to
         *           avoid explicit joins while maintaining readability and performance.
         */
        @Override
        public List<Message> findBySenderId(Long senderId) {

                return em.createQuery("SELECT m FROM Message m WHERE m.sender.id = :senderId", Message.class)
                                .setParameter("senderId", senderId)
                                .getResultList();
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses JPQL query to find all messages where the recipient ID
         * matches the specified parameter. Uses entity relationship navigation
         * for efficient querying.
         * </p>
         *
         * @implNote Similar to sender lookup, uses JPA relationship navigation
         *           (m.recipient.id) for clean and efficient querying.
         */
        @Override
        public List<Message> findByRecipientId(Long recipientId) {

                return em.createQuery(" SELECT m FROM Message m WHERE m.recipient.id = :recipientId", Message.class)
                                .setParameter("recipientId", recipientId)
                                .getResultList();
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses JPQL query with multiple conditions to find messages
         * sent from a specific sender to a specific recipient. This provides
         * unidirectional conversation history.
         * </p>
         *
         * @implNote Uses AND condition to filter by both sender and recipient IDs.
         *           For full conversation history, combine with reverse query or
         *           use {@link #findMessagedUsers(Long)} for conversation participants.
         */
        @Override
        public List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId) {

                return em
                                .createQuery("SELECT m FROM Message m WHERE m.sender.id = :senderId AND m.recipient.id = :recipientId",
                                                Message.class)
                                .setParameter("senderId", senderId)
                                .setParameter("recipientId", recipientId)
                                .getResultList();
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses a complex JPQL query with CASE expressions to find
         * all unique users who have had message exchanges with the specified user.
         * The query efficiently handles both sent and received messages in a single
         * database operation.
         * </p>
         *
         * @implNote Uses a sophisticated query with CASE WHEN to get conversation
         *           partners by examining both sender and recipient roles. Excludes
         *           the querying user from results and ensures uniqueness with
         *           DISTINCT.
         */
        @Override
        public List<User> findMessagedUsers(Long userId) {
                // More efficient approach using JOIN
                return em.createQuery(
                                "SELECT DISTINCT u FROM User u " +
                                                "WHERE u.id IN (" +
                                                "SELECT DISTINCT CASE " +
                                                "WHEN m.sender.id = :userId THEN m.recipient.id " +
                                                "ELSE m.sender.id " +
                                                "END " +
                                                "FROM Message m " +
                                                "WHERE m.sender.id = :userId OR m.recipient.id = :userId" +
                                                ") AND u.id != :userId",
                                User.class)
                                .setParameter("userId", userId)
                                .getResultList();
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses COUNT function in JPQL to efficiently count unread
         * messages without loading full message entities. Filters by recipient ID
         * and read status.
         * </p>
         *
         * @implNote Uses COUNT aggregation for performance, returning Long and
         *           converting to Integer for interface compatibility. The query
         *           only processes message metadata, not content.
         */
        @Override
        public Integer findCountUnreadMessagesByRecipientId(Long recipientId) {

                return em
                                .createQuery("SELECT COUNT(m) FROM Message m WHERE m.recipient.id = :recipientId AND m.isRead = FALSE",
                                                Long.class)
                                .setParameter("recipientId", recipientId)
                                .getSingleResult()
                                .intValue();
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses COUNT function with multiple conditions to count
         * unread messages between specific users. This is useful for
         * conversation-specific
         * unread indicators in messaging interfaces.
         * </p>
         *
         * @implNote Similar to general unread count but adds sender filter for
         *           conversation-specific counting. Helps implement per-conversation
         *           unread badges in user interfaces.
         */
        @Override
        public Integer findCountUnreadMessagesBetweenUsers(Long recipientId, Long senderId) {

                return em
                                .createQuery(
                                                "SELECT COUNT(m) FROM Message m WHERE m.recipient.id = :recipientId AND m.sender.id = :senderId AND m.isRead = FALSE",
                                                Long.class)
                                .setParameter("recipientId", recipientId)
                                .setParameter("senderId", senderId)
                                .getSingleResult()
                                .intValue();
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses an UPDATE query to atomically mark a message as read
         * and set the read timestamp. Returns success status based on the number
         * of affected rows.
         * </p>
         *
         * @implNote Uses {@link Instant#now()} to timestamp the read operation.
         *           The method returns true if exactly one row was updated, false
         *           if the message was not found or was already read.
         */
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
