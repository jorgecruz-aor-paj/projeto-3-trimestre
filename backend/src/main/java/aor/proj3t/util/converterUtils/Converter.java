package aor.proj3t.util.converterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic interface for bidirectional conversion between entities and DTOs.
 * 
 * This interface defines the contract for converting between domain entities
 * and data transfer objects (DTOs).
 * It provides methods for converting individual objects as well as collections
 * of objects in both directions.
 * Implementing classes should provide efficient and consistent conversion logic
 * that preserves the integrity
 * of the data being transferred.
 * 
 * @param <E> The entity type (domain model object)
 * @param <D> The DTO type (data transfer object)
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
public interface Converter<E, D> {
    /**
     * Converts an entity to its corresponding DTO.
     * 
     * This method transforms a domain entity into a data transfer object that can
     * be safely
     * used in the presentation layer or API responses. It extracts the relevant
     * data from
     * the entity while hiding implementation details and sensitive information.
     * 
     * @param entity The entity to convert
     * @return The converted DTO, or null if entity is null
     */
    D toDTO(E entity);

    /**
     * Converts a DTO to its corresponding entity.
     * 
     * This method transforms a data transfer object into a domain entity that can
     * be
     * used by business logic or persisted in a database. It applies business rules
     * and
     * validates the data during the conversion process, ensuring that the resulting
     * entity
     * is in a valid state.
     * 
     * @param dto The DTO to convert
     * @return The converted entity, or null if dto is null
     */
    E toEntity(D dto);

    /**
     * Converts a list of entities to a list of DTOs.
     * 
     * This method transforms a collection of domain entities into a collection of
     * data transfer objects. It provides a convenient way to convert entire
     * collections
     * in a single operation. If the input list is null, an empty list is returned.
     * 
     * @param entityList The list of entities to convert
     * @return A list of converted DTOs, never null
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
     * Converts a list of DTOs to a list of entities.
     * 
     * This method transforms a collection of data transfer objects into a
     * collection of
     * domain entities. It provides a convenient way to convert entire collections
     * in a
     * single operation. If the input list is null, an empty list is returned.
     * 
     * @param dtoList The list of DTOs to convert
     * @return A list of converted entities, never null
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
