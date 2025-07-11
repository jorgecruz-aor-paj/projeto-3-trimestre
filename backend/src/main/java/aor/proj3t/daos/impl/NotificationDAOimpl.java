package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.List;

import aor.proj3t.daos.interfaces.NotificationDAO;
import aor.proj3t.entities.Notification;
import jakarta.ejb.Stateless;

/**
 * Stateless EJB implementation of the {@link NotificationDAO} interface.
 * <p>
 * This class provides concrete implementation for managing system notifications
 * within the application. It extends {@link AbstractDAO} to leverage common
 * CRUD operations and implements specific business logic for notification
 * delivery, pagination, and bulk operations.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Optimized queries with automatic ordering by timestamp</li>
 * <li>Pagination support for large notification lists</li>
 * <li>Efficient counting operations for pagination controls</li>
 * <li>Bulk marking operations with atomic timestamp updates</li>
 * <li>Performance-optimized queries using recipient-based filtering</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see NotificationDAO
 * @see AbstractDAO
 * @see Notification
 */
@Stateless
public class NotificationDAOimpl extends AbstractDAO<Notification, Long> implements NotificationDAO {

        /**
         * {@inheritDoc}
         * 
         * @implNote Returns {@code Notification.class} to enable type-safe operations
         *           throughout the DAO implementation.
         */
        @Override
        protected Class<Notification> getEntityClass() {
                return Notification.class;
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses JPQL query with ORDER BY clause to retrieve notifications
         * sorted by timestamp in descending order (most recent first). This provides
         * a natural ordering for notification display in user interfaces.
         * </p>
         *
         * @implNote For large notification lists, consider using the paginated version
         *           {@link #findByRecipientIdPaginated(Long, int, int)} to avoid
         *           memory issues and improve performance.
         */
        @Override
        public List<Notification> findByRecipientId(Long recipientId) {

                return em.createQuery(
                                "SELECT n FROM Notification n WHERE n.recipient.id = :recipientId ORDER BY n.timestampSent DESC",
                                Notification.class)
                                .setParameter("recipientId", recipientId)
                                .getResultList();
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses JPQL query with LIMIT and OFFSET clauses for efficient
         * pagination. Results are ordered by timestamp in descending order to maintain
         * consistent ordering across page boundaries.
         * </p>
         *
         * @implNote Uses {@link jakarta.persistence.TypedQuery#setFirstResult(int)}
         *           and {@link jakarta.persistence.TypedQuery#setMaxResults(int)} for
         *           database-level pagination, which is more efficient than loading
         *           all results and filtering in application memory.
         */
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

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses COUNT function in JPQL for efficient counting without
         * loading full notification entities. Essential for pagination controls and
         * displaying total counts in user interfaces.
         * </p>
         *
         * @implNote Uses aggregation function for optimal performance as only the
         *           count value is needed, not the actual notification data.
         */
        @Override
        public Long countByRecipientId(Long recipientId) {
                return em.createQuery("SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :recipientId",
                                Long.class)
                                .setParameter("recipientId", recipientId)
                                .getSingleResult();
        }

        /**
         * {@inheritDoc}
         * <p>
         * Implementation uses a bulk UPDATE query to atomically mark all unread
         * notifications as read and set the read timestamp. This provides efficient
         * "mark all as read" functionality for user interfaces.
         * </p>
         *
         * @implNote Uses {@link Instant#now()} for consistent timestamping and
         *           conditions the update on isRead = FALSE to avoid unnecessary
         *           updates of already-read notifications. Returns the actual
         *           number of notifications that were updated.
         */
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
