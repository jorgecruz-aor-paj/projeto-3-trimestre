package aor.proj3t.DAOs;


import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for CRUD operations.
 *
 * @param <T>  The entity type
 * @param <ID> The entity's ID type
 */
public interface BasicDAO<T, ID> {

    /**
     * Create a new entity.
     *
     * @param entity The entity to create
     * @return The created entity with its generated ID
     */
    T create(T entity);

    /**
     * Update an existing entity.
     *
     * @param entity The entity to update
     * @return The updated entity
     */
    T update(T entity);

    /**
     * Find an entity by its ID.
     *
     * @param id The ID of the entity to find
     * @return An Optional containing the entity if found, or empty if not found
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities.
     *
     * @return A list of all entities
     */
    List<T> findAll();

    /**
     * Delete an entity by its ID.
     *
     * @param id The ID of the entity to delete
     * @return true if the entity was deleted, false otherwise
     */
    boolean deleteById(ID id);
}

