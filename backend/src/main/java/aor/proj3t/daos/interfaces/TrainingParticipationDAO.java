package aor.proj3t.daos.interfaces;

import java.util.LinkedHashMap;
import java.util.List;

import aor.proj3t.entities.TrainingParticipation;

/**
 * Data Access Object interface for TrainingParticipation entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide comprehensive operations
 * for managing training participation records. TrainingParticipation entities
 * represent the relationship between users and training programs, tracking
 * enrollment, completion status, and participation metrics.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Querying participation records by participant</li>
 * <li>Training time analysis and reporting</li>
 * <li>Participation history tracking</li>
 * <li>Performance analytics and insights</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see TrainingParticipation
 * @see BasicDAO
 */
public interface TrainingParticipationDAO extends BasicDAO<TrainingParticipation, Long> {

    /**
     * Retrieves all training participation records for a specific participant.
     * <p>
     * This method finds all training programs that a user has participated in
     * or is currently enrolled in. Essential for displaying user training
     * history, tracking learning progress, and generating individual training
     * reports.
     * </p>
     *
     * @param participantId The unique identifier of the participant. Must not be
     *                      null.
     * @return A {@link List} of training participation records for the specified
     *         participant,
     *         or an empty list if no participations are found
     * @throws IllegalArgumentException               if participantId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<TrainingParticipation> findByParticipantId(Long participantId);

    /**
     * Calculates total training time by year for a specific participant.
     * <p>
     * This method provides analytics on training time investment over multiple
     * years.
     * The returned map contains year-to-training-time mappings, where keys
     * represent
     * calendar years and values represent total training hours or duration for that
     * year.
     * Results are ordered chronologically for trend analysis.
     * </p>
     *
     * @param participationId The unique identifier of the participant. Must not be
     *                        null.
     * @return A {@link LinkedHashMap} where keys are years (Integer) and values are
     *         total training time (Long) for each year, ordered chronologically.
     *         Returns an empty map if no training data exists.
     * @throws IllegalArgumentException               if participationId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    LinkedHashMap<Integer, Long> trainingTimeByYearByParticipantId(Long participationId);

}
