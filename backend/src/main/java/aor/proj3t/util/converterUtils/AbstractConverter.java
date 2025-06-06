package aor.proj3t.util.converterUtils;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for entity-DTO converters that implements common functionality
 * 
 * @param <E> The entity type
 * @param <D> The DTO type
 * @param <ID> The type of the entity's ID field
 */
public abstract class AbstractConverter<E, D, ID> implements Converter<E, D> {

    @Inject
    protected EntityManager em;
    
    /**
     * Get the class of the entity
     * 
     * @return The entity class
     */
    protected abstract Class<E> getEntityClass();
    
    /**
     * Create a new instance of the entity
     * 
     * @return A new entity instance
     */
    protected abstract E createEntityInstance();
    
    /**
     * Create a new instance of the DTO
     * 
     * @return A new DTO instance
     */
    protected abstract D createDTOInstance();
    
    /**
     * Get the ID from the DTO
     * 
     * @param dto The DTO
     * @return The ID value
     */
    protected abstract ID getIdFromDTO(D dto);
    
    /**
     * Map entity fields to DTO fields
     * 
     * @param entity The source entity
     * @param dto The target DTO
     */
    protected abstract void mapEntityToDTO(E entity, D dto);
    
    /**
     * Map DTO fields to entity fields
     * 
     * @param dto The source DTO
     * @param entity The target entity
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
     * Safely finds an entity by ID, returning null if not found
     * 
     * @param <T> Entity type
     * @param entityClass The class of the entity
     * @param id The ID to look up
     * @return The found entity or null
     */
    protected <T> T findSafely(Class<T> entityClass, Object id) {
        if (id == null) {
            return null;
        }
        return em.find(entityClass, id);
    }
}