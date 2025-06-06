package aor.proj3t.util.converterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Abstract base class for entity-DTO converters that implements common
 * functionality.
 * 
 * This class provides a template implementation of the {@link Converter}
 * interface,
 * implementing the conversion algorithms while allowing subclasses to define
 * specific mapping logic through abstract methods. It introduces the Template
 * Method
 * pattern to standardize the overall conversion process while allowing
 * customization
 * of specific steps.
 * 
 * Key features:
 * <ul>
 * <li>Implements the basic conversion algorithms for both individual objects
 * and collections</li>
 * <li>Handles null values safely</li>
 * <li>Manages entity lookup, creation, and relationship handling</li>
 * <li>Provides utility methods for common operations like safe entity
 * lookup</li>
 * </ul>
 * * @param <E> The entity type (domain model object)
 * 
 * @param <D>  The DTO type (data transfer object)
 * @param <ID> The type of the entity's ID field
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @see Converter
 */
public abstract class AbstractConverter<E, D, ID> implements Converter<E, D> {

    @Inject
    protected EntityManager em;

    /**
     * Gets the class of the entity.
     * 
     * This method returns the Class object for the entity type E. It's used
     * by the conversion process to find entities in the persistence context
     * and for reflection-based operations.
     * 
     * @return The entity class
     */
    protected abstract Class<E> getEntityClass();

    /**
     * Creates a new instance of the entity.
     * 
     * This method is responsible for instantiating a new entity object when
     * a conversion process determines that a new entity needs to be created.
     * Subclasses should implement this method to return a fresh instance of the
     * appropriate entity class.
     * 
     * @return A new entity instance
     */
    protected abstract E createEntityInstance();

    /**
     * Creates a new instance of the DTO.
     * 
     * This method is responsible for instantiating a new DTO object when
     * a conversion process requires a fresh DTO. Subclasses should implement
     * this method to return a new instance of the appropriate DTO class.
     * 
     * @return A new DTO instance
     */
    protected abstract D createDTOInstance();

    /**
     * Gets the ID from the DTO.
     * 
     * This method extracts the identifier value from a DTO. The identifier is used
     * to determine if the DTO represents an existing entity that should be updated
     * or if a new entity should be created. Subclasses may implement custom logic
     * to determine what constitutes a valid ID (e.g., non-zero values only).
     * 
     * @param dto The DTO to extract the ID from
     * @return The ID value, or null if the DTO has no valid ID
     */
    protected abstract ID getIdFromDTO(D dto);

    /**
     * Maps entity fields to DTO fields.
     * 
     * This method is responsible for transferring data from an entity object to a
     * DTO object.
     * It should map all relevant fields from the entity to their corresponding
     * fields in the DTO,
     * including handling of relationships. Implementations should be careful to
     * handle null
     * values and ensure that all necessary data is copied.
     * 
     * @param entity The source entity containing the data to be transferred
     * @param dto    The target DTO to receive the data
     */
    protected abstract void mapEntityToDTO(E entity, D dto);

    /**
     * Maps DTO fields to entity fields.
     * 
     * This method is responsible for transferring data from a DTO object to an
     * entity object.
     * It should map all relevant fields from the DTO to their corresponding fields
     * in the entity,
     * including handling of relationships. Implementations should be careful to
     * handle null
     * values and ensure that all necessary data is copied. Use the
     * {@link #findSafely} method
     * to handle relationships safely.
     * 
     * @param dto    The source DTO containing the data to be transferred
     * @param entity The target entity to receive the data
     */
    protected abstract void mapDTOToEntity(D dto, E entity);

    @Override
    public D toDTO(E entity) {
        if (entity == null) {
            return null;
        }

        D dto = createDTOInstance();
        mapEntityToDTO(entity, dto);
        return dto;
    }

    @Override
    public E toEntity(D dto) {
        if (dto == null) {
            return null;
        }

        E entity;
        ID id = getIdFromDTO(dto);

        if (id != null) {
            // Try to find existing entity
            entity = em.find(getEntityClass(), id);
            if (entity == null) {
                entity = createEntityInstance();
            }
        } else {
            entity = createEntityInstance();
        }

        mapDTOToEntity(dto, entity);
        return entity;
    }

    @Override
    public List<D> toDTOList(List<E> entityList) {
        if (entityList == null) {
            return Collections.emptyList();
        }

        List<D> dtoList = new ArrayList<>(entityList.size());
        for (E entity : entityList) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    @Override
    public List<E> toEntityList(List<D> dtoList) {
        if (dtoList == null) {
            return Collections.emptyList();
        }

        List<E> entityList = new ArrayList<>(dtoList.size());
        for (D dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }

    /**
     * Safely finds an entity by ID, returning null if not found.
     * 
     * This utility method provides a safe way to look up related entities during
     * the
     * conversion process. It handles null ID values gracefully by returning null
     * instead
     * of throwing an exception, which simplifies relationship handling code.
     * 
     * <p>
     * Example usage:
     * 
     * <pre>
     * User manager = findSafely(User.class, dto.getManagerId());
     * entity.setManager(manager);
     * </pre>
     * 
     * @param <T>         Entity type parameter
     * @param entityClass The class of the entity to look up
     * @param id          The ID to look up
     * @return The found entity or null if either the ID is null or no entity with
     *         that ID exists
     */
    protected <T> T findSafely(Class<T> entityClass, Object id) {
        if (id == null) {
            return null;
        }
        return em.find(entityClass, id);
    }
}