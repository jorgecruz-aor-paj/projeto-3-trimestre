package aor.proj3t.daos.impl;

import java.util.LinkedHashMap;
import java.util.List;

import aor.proj3t.daos.interfaces.TrainingParticipationDAO;
import aor.proj3t.entities.TrainingParticipation;
import jakarta.ejb.Stateless;

/**
 * Stateless EJB implementation of the {@link TrainingParticipationDAO}
 * interface.
 * <p>
 * This class provides concrete implementation for managing training
 * participation
 * records within the application. It extends {@link AbstractDAO} to leverage
 * common
 * CRUD operations and implements specific business logic for tracking employee
 * training participation, analytics, and reporting.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Participant-based training history retrieval</li>
 * <li>Time-based training analytics with yearly aggregation</li>
 * <li>JPQL queries for efficient participation filtering</li>
 * <li>Aggregate functions for training duration calculations</li>
 * <li>Ordered results for chronological analysis</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see TrainingParticipationDAO
 * @see AbstractDAO
 * @see TrainingParticipation
 */
@Stateless
public class TrainingParticipationDAOimpl extends AbstractDAO<TrainingParticipation, Long>
        implements TrainingParticipationDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code TrainingParticipation.class} to enable type-safe
     *           operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<TrainingParticipation> getEntityClass() {
        return TrainingParticipation.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses JPQL query to find all training participation records
     * for a specific participant. Uses entity relationship navigation for efficient
     * querying without explicit joins.
     * </p>
     *
     * @implNote The query leverages JPA entity relationships (tp.participant.id) to
     *           filter participations by participant ID. Results include all
     *           training programs the user has participated in, regardless of
     *           status.
     */
    @Override
    public List<TrainingParticipation> findByParticipantId(Long participantId) {
        String query = "SELECT tp FROM TrainingParticipation tp WHERE tp.participant.id = :participantId";
        return em.createQuery(query, TrainingParticipation.class)
                .setParameter("participantId", participantId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses a complex JPQL query with aggregate functions to
     * calculate
     * total training time per year for a specific participant. The query groups
     * results by year and sums training durations, providing valuable analytics
     * for training investment tracking.
     * </p>
     * <p>
     * The method processes the query results to build a LinkedHashMap that
     * maintains
     * chronological ordering (most recent years first) for easy consumption by
     * reporting and analytics components.
     * </p>
     *
     * @implNote Uses YEAR() function for date extraction and SUM() for duration
     *           aggregation. Results are ordered by year in descending order to
     *           show recent training activity first. The LinkedHashMap preserves
     *           insertion order for consistent result presentation.
     */
    @Override
    public LinkedHashMap<Integer, Long> trainingTimeByYearByParticipantId(Long participantId) {
        String query = """
                    SELECT YEAR(tp.participatedAt), SUM(tp.training.duration)
                    FROM TrainingParticipation tp
                    WHERE tp.participant.id = :participantId
                    GROUP BY YEAR(tp.participatedAt)
                    ORDER BY YEAR(tp.participatedAt) DESC
                """;

        List<Object[]> results = em.createQuery(query, Object[].class)
                .setParameter("participantId", participantId)
                .getResultList();

        LinkedHashMap<Integer, Long> trainingTimeByYear = new LinkedHashMap<>();
        for (Object[] row : results) {
            trainingTimeByYear.put((Integer) row[0], ((Long) row[1]));
        }

        return trainingTimeByYear;
    }

}
