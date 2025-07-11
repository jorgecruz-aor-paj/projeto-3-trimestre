package aor.proj3t.daos.interfaces;

import java.util.Optional;

import aor.proj3t.entities.TrainingArea;

/**
 * Data Access Object interface for TrainingArea entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide operations for managing
 * training areas or categories within the training management system. Training
 * areas represent different domains, subjects, or categories of training
 * programs available in the organization.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Finding training areas by name for categorization</li>
 * <li>Managing training program categories</li>
 * <li>Supporting training organization and filtering</li>
 * <li>Hierarchical training area management</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see TrainingArea
 * @see BasicDAO
 */
public interface TrainingAreaDAO extends BasicDAO<TrainingArea, Long> {

    /**
     * Finds a training area by its name.
     * <p>
     * This method locates a training area using its name as the search criteria.
     * Training area names are typically unique within the system and serve as
     * a natural identifier for categorizing training programs.
     * </p>
     *
     * @param name The name of the training area to search for. Must not be null or
     *             empty.
     * @return An {@link Optional} containing the training area if found,
     *         or {@link Optional#empty()} if no training area with the name exists
     * @throws IllegalArgumentException               if name is null or empty
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Optional<TrainingArea> findByName(String name);

}
