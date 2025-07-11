package aor.proj3t.unit.util.converterutils;

import aor.proj3t.util.converterutils.Converter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Base test class for all converter utilities.
 * Provides common test patterns and methods that can be reused across all converter tests.
 * 
 * @param <E> The entity type
 * @param <D> The DTO type
 * @param <C> The converter type
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseConverterTest<E, D, C extends Converter<E, D>> {

    protected abstract C getConverter();
    protected abstract E createValidEntity();
    protected abstract D createValidDTO();
    protected abstract void assertEntityEqualsDTO(E entity, D dto);
    protected abstract void assertDTOEqualsEntity(D dto, E entity);

    @Test
    @DisplayName("Should convert entity to DTO successfully")
    public void testEntityToDTO() {
        // Given
        E entity = createValidEntity();
        
        // When
        D dto = getConverter().toDTO(entity);
        
        // Then
        assertNotNull(dto);
        assertEntityEqualsDTO(entity, dto);
    }

    @Test
    @DisplayName("Should convert DTO to entity successfully")
    public void testDTOToEntity() {
        // Given
        D dto = createValidDTO();
        
        // When
        E entity = getConverter().toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertDTOEqualsEntity(dto, entity);
    }

    @Test
    @DisplayName("Should handle null entity conversion gracefully")
    public void testNullEntityToDTO() {
        // When
        D dto = getConverter().toDTO(null);
        
        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Should handle null DTO conversion gracefully")
    public void testNullDTOToEntity() {
        // When
        E entity = getConverter().toEntity(null);
        
        // Then
        assertNull(entity);
    }

    @Test
    @DisplayName("Should convert entity list to DTO list successfully")
    public void testEntityListToDTOList() {
        // Given
        E entity1 = createValidEntity();
        E entity2 = createValidEntity();
        List<E> entityList = Arrays.asList(entity1, entity2);
        
        // When
        List<D> dtoList = getConverter().toDTOList(entityList);
        
        // Then
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertNotNull(dtoList.get(0));
        assertNotNull(dtoList.get(1));
    }

    @Test
    @DisplayName("Should convert DTO list to entity list successfully")
    public void testDTOListToEntityList() {
        // Given
        D dto1 = createValidDTO();
        D dto2 = createValidDTO();
        List<D> dtoList = Arrays.asList(dto1, dto2);
        
        // When
        List<E> entityList = getConverter().toEntityList(dtoList);
        
        // Then
        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertNotNull(entityList.get(0));
        assertNotNull(entityList.get(1));
    }

    @Test
    @DisplayName("Should handle null entity list conversion gracefully")
    public void testNullEntityListToDTOList() {
        // When
        List<D> dtoList = getConverter().toDTOList(null);
        
        // Then
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    @DisplayName("Should handle null DTO list conversion gracefully")
    public void testNullDTOListToEntityList() {
        // When
        List<E> entityList = getConverter().toEntityList(null);
        
        // Then
        assertNotNull(entityList);
        assertTrue(entityList.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty entity list conversion")
    public void testEmptyEntityListToDTOList() {
        // Given
        List<E> entityList = Collections.emptyList();
        
        // When
        List<D> dtoList = getConverter().toDTOList(entityList);
        
        // Then
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty DTO list conversion")
    public void testEmptyDTOListToEntityList() {
        // Given
        List<D> dtoList = Collections.emptyList();
        
        // When
        List<E> entityList = getConverter().toEntityList(dtoList);
        
        // Then
        assertNotNull(entityList);
        assertTrue(entityList.isEmpty());
    }

    @Test
    @DisplayName("Should maintain data integrity in round-trip conversion")
    public void testRoundTripConversion() {
        // Given
        E originalEntity = createValidEntity();
        
        // When
        D dto = getConverter().toDTO(originalEntity);
        E convertedEntity = getConverter().toEntity(dto);
        
        // Then
        assertNotNull(dto);
        assertNotNull(convertedEntity);
        // Note: Exact equality depends on implementation
        // Subclasses should override if needed
    }
}
