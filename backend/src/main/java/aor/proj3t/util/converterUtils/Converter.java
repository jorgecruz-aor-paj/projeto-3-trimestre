package aor.proj3t.util.converterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic interface for bidirectional conversion between entities and DTOs
 * 
 * @param <E> The entity type
 * @param <D> The DTO type
 */
public interface Converter<E, D> {
    /**
     * Convert an entity to its corresponding DTO
     * 
     * @param entity The entity to convert
     * @return The converted DTO, or null if entity is null
     */
    D toDTO(E entity);

    /**
     * Convert a DTO to its corresponding entity
     * 
     * @param dto The DTO to convert
     * @return The converted entity, or null if dto is null
     */
    E toEntity(D dto);

    /**
     * Convert a list of entities to a list of DTOs
     * 
     * @param entityList The list of entities to convert
     * @return A list of converted DTOs
     */
    default List<D> toDTOList(List<E> entityList) {
        if (entityList == null) {
            return Collections.emptyList();
        }

        List<D> dtoList = new ArrayList<>(entityList.size());
        for (E entity : entityList) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    /**
     * Convert a list of DTOs to a list of entities
     * 
     * @param dtoList The list of DTOs to convert
     * @return A list of converted entities
     */
    default List<E> toEntityList(List<D> dtoList) {
        if (dtoList == null) {
            return Collections.emptyList();
        }

        List<E> entityList = new ArrayList<>(dtoList.size());
        for (D dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }
}
