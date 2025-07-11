package aor.proj3t.daos.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for CRUD operations on entities.
 * <p>
 * This interface provides the basic data access operations that are common
 * to all entity types in the application. It follows the Repository pattern
 * and provides type-safe operations through generics.
 * </p>
 * <p>
 * All specific DAO interfaces should extend this interface to inherit
 * the basic CRUD functionality while adding their own domain-specific
 * operations.
 * </p>
 *
 * @param <T>  The entity type that this DAO manages
 * @param <ID> The type of the entity's primary key identifier
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public interface BasicDAO<T, ID> {

    /**
     * Creates a new entity in the data store.
     * <p>
     * This method persists a new entity to the underlying data store.
     * The entity will be assigned a unique identifier if it doesn't already have
     * one.
     * </p>
     *
     * @param entity The entity to create. Must not be null.
     * @return The created entity with its generated ID and any other auto-generated
     *         fields populated
     * @throws IllegalArgumentException                      if the entity is null
     * @throws javax.persistence.PersistenceException        if a database access
     *                                                       error occurs
     * @throws javax.validation.ConstraintViolationException if the entity violates
     *                                                       validation constraints
     */
    T create(T entity);

    /**
     * Updates an existing entity in the data store.
     * <p>
     * This method merges the state of the given entity with the persistent state.
     * The entity must have a valid identifier that corresponds to an existing
     * record.
     * </p>
     *
     * @param entity The entity to update. Must not be null and must have a valid
     *               ID.
     * @return The updated entity, potentially with updated version fields or
     *         timestamps
     * @throws IllegalArgumentException                      if the entity is null
     *                                                       or has no ID
     * @throws javax.persistence.EntityNotFoundException     if no entity with the
     *                                                       given ID exists
     * @throws javax.persistence.PersistenceException        if a database access
     *                                                       error occurs
     * @throws javax.validation.ConstraintViolationException if the entity violates
     *                                                       validation constraints
     */
    T update(T entity);

    /**
     * Finds an entity by its unique identifier.
     * <p>
     * This method performs a lookup for an entity with the specified ID.
     * The operation is read-only and does not modify the entity state.
     * </p>
     *
     * @param id The unique identifier of the entity to find. Must not be null.
     * @return An {@link Optional} containing the entity if found, or
     *         {@link Optional#empty()} if not found
     * @throws IllegalArgumentException               if the ID is null
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Optional<T> findById(ID id);

    /**
     * Retrieves all entities of this type from the data store.
     * <p>
     * This method returns all entities managed by this DAO. Use with caution
     * for large datasets as it may impact performance and memory usage.
     * Consider using pagination methods in specific DAO implementations for large
     * result sets.
     * </p>
     *
     * @return A {@link List} containing all entities, or an empty list if no
     *         entities exist
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    List<T> findAll();

    /**
     * Deletes an entity by its unique identifier.
     * <p>
     * This method removes the entity with the specified ID from the data store.
     * The operation is permanent and cannot be undone.
     * </p>
     *
     * @param id The unique identifier of the entity to delete. Must not be null.
     * @return {@code true} if the entity was successfully deleted, {@code false} if
     *         no entity with the given ID was found
     * @throws IllegalArgumentException                  if the ID is null
     * @throws javax.persistence.PersistenceException    if a database access error
     *                                                   occurs
     * @throws javax.persistence.EntityNotFoundException in some implementations if
     *                                                   the entity doesn't exist
     */
    boolean deleteById(ID id);
}
