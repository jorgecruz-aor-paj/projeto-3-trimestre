package aor.proj3t.daos.interfaces;

import java.util.List;

import aor.proj3t.entities.Training;
import aor.proj3t.filters.TrainingFilter;

/**
 * Data Access Object interface for Training entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide comprehensive operations
 * for managing training programs within the organization. Training entities
 * represent educational programs, courses, workshops, or skill development
 * activities available to employees.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Advanced filtering using {@link TrainingFilter} for complex queries</li>
 * <li>Finding available trainings for specific users</li>
 * <li>Training enrollment and participation management</li>
 * <li>Training catalog and discovery functionality</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see Training
 * @see TrainingFilter
 * @see BasicDAO
 */
public interface TrainingDAO extends BasicDAO<Training, Long> {

    /**
     * Retrieves trainings based on complex filtering criteria.
     * <p>
     * This method provides advanced querying capabilities using a filter object
     * that can specify multiple criteria such as training area, date ranges,
     * status, location, and other training properties. Essential for implementing
     * search and filtering functionality in training catalogs.
     * </p>
     *
     * @param filters The filter criteria to apply. Must not be null.
     * @return A {@link List} of trainings matching the filter criteria,
     *         or an empty list if no matches are found
     * @throws IllegalArgumentException               if filters is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     * @see TrainingFilter
     */
    List<Training> getTrainingsByFilters(TrainingFilter filters);

    /**
     * Finds trainings that a specific user has not yet participated in.
     * <p>
     * This method returns available training opportunities for a user by excluding
     * trainings they have already completed or are currently enrolled in.
     * Useful for recommending new training programs and displaying available
     * learning opportunities.
     * </p>
     *
     * @param userId The unique identifier of the user. Must not be null.
     * @return A {@link List} of trainings not participated by the user,
     *         or an empty list if no available trainings exist
     * @throws IllegalArgumentException               if userId is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<Training> findTrainingsNotParticipatedByUser(Long userId);

}
