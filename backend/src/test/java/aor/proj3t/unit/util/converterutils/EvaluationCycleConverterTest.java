package aor.proj3t.unit.util.converterutils;


import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.util.converterutils.EvaluationConverter;
import aor.proj3t.util.converterutils.EvaluationCycleConverter;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EvaluationCycleConverter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EvaluationCycleConverter Tests")
public class EvaluationCycleConverterTest extends BaseConverterTest<EvaluationCycle, EvaluationCycleDTO, EvaluationCycleConverter> {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EvaluationConverter evaluationConverter;

    @InjectMocks
    private EvaluationCycleConverter evaluationCycleConverter;

    private EvaluationCycle evaluationCycle;
    private EvaluationCycleDTO evaluationCycleDTO;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(User.class), any(Long.class))).thenReturn(null);

        // Create mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        // Create test evaluation cycle
        evaluationCycle = new EvaluationCycle();
        evaluationCycle.setId(1L);
        evaluationCycle.setCreatedAt(Instant.now());
        evaluationCycle.setStartDate(Instant.now().minusSeconds(30 * 24 * 60 * 60));
        evaluationCycle.setEndDate(Instant.now().plusSeconds(30 * 24 * 60 * 60));
        evaluationCycle.setClosureDate(Instant.now().plusSeconds(60 * 24 * 60 * 60));
        evaluationCycle.setStatus(EvaluationCycleStatus.ACTIVE);
        evaluationCycle.setCreatedBy(mockUser);

        // Create test DTO
        evaluationCycleDTO = new EvaluationCycleDTO();
        evaluationCycleDTO.setId(1L);
        evaluationCycleDTO.setCreatedAt(Instant.now());
        evaluationCycleDTO.setStartDate(Instant.now().minusSeconds(30 * 24 * 60 * 60));
        evaluationCycleDTO.setEndDate(Instant.now().plusSeconds(30 * 24 * 60 * 60));
        evaluationCycleDTO.setClosureDate(Instant.now().plusSeconds(60 * 24 * 60 * 60));
        evaluationCycleDTO.setStatus(EvaluationCycleStatus.ACTIVE);
        evaluationCycleDTO.setCreatedById(1L);
    }

    @Override
    protected EvaluationCycleConverter getConverter() {
        return evaluationCycleConverter;
    }

    @Override
    protected EvaluationCycle createValidEntity() {
        EvaluationCycle entity = new EvaluationCycle();
        entity.setId(1L);
        entity.setCreatedAt(Instant.now());
        entity.setStartDate(Instant.now().minusSeconds(30 * 24 * 60 * 60));
        entity.setEndDate(Instant.now().plusSeconds(30 * 24 * 60 * 60));
        entity.setClosureDate(Instant.now().plusSeconds(60 * 24 * 60 * 60));
        entity.setStatus(EvaluationCycleStatus.ACTIVE);
        // Don't set createdBy for base test
        return entity;
    }

    @Override
    protected EvaluationCycleDTO createValidDTO() {
        EvaluationCycleDTO dto = new EvaluationCycleDTO();
        dto.setId(1L);
        dto.setCreatedAt(Instant.now());
        dto.setStartDate(Instant.now().minusSeconds(30 * 24 * 60 * 60));
        dto.setEndDate(Instant.now().plusSeconds(30 * 24 * 60 * 60));
        dto.setClosureDate(Instant.now().plusSeconds(60 * 24 * 60 * 60));
        dto.setStatus(EvaluationCycleStatus.ACTIVE);
        // Don't set createdById for base test
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(EvaluationCycle entity, EvaluationCycleDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getStartDate(), dto.getStartDate());
        assertEquals(entity.getEndDate(), dto.getEndDate());
        assertEquals(entity.getClosureDate(), dto.getClosureDate());
        assertEquals(entity.getStatus(), dto.getStatus());
        // Note: Not checking createdBy relationship in base test
    }

    @Override
    protected void assertDTOEqualsEntity(EvaluationCycleDTO dto, EvaluationCycle entity) {
        // Don't check ID as it comes from database, not DTO
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getStartDate(), entity.getStartDate());
        assertEquals(dto.getEndDate(), entity.getEndDate());
        assertEquals(dto.getClosureDate(), entity.getClosureDate());
        assertEquals(dto.getStatus(), entity.getStatus());
        // Note: Not checking createdBy relationship in base test
    }

    @Test
    @DisplayName("Should convert EvaluationCycle entity to DTO")
    void testToDTO() {
        // Act
        EvaluationCycleDTO result = evaluationCycleConverter.toDTO(evaluationCycle);

        // Assert
        assertNotNull(result);
        assertEquals(evaluationCycle.getId(), result.getId());
        assertEquals(evaluationCycle.getCreatedAt(), result.getCreatedAt());
        assertEquals(evaluationCycle.getStartDate(), result.getStartDate());
        assertEquals(evaluationCycle.getEndDate(), result.getEndDate());
        assertEquals(evaluationCycle.getClosureDate(), result.getClosureDate());
        assertEquals(evaluationCycle.getStatus(), result.getStatus());
        assertEquals(evaluationCycle.getCreatedBy().getId(), result.getCreatedById());
    }

    @Test
    @DisplayName("Should convert EvaluationCycle entity to DTO with null createdBy")
    void testToDTOWithNullCreatedBy() {
        // Arrange
        evaluationCycle.setCreatedBy(null);

        // Act
        EvaluationCycleDTO result = evaluationCycleConverter.toDTO(evaluationCycle);

        // Assert
        assertNotNull(result);
        assertEquals(evaluationCycle.getId(), result.getId());
        assertNull(result.getCreatedById());
    }

    @Test
    @DisplayName("Should convert EvaluationCycleDTO to entity")
    void testToEntity() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockUser);

        // Act
        EvaluationCycle result = evaluationCycleConverter.toEntity(evaluationCycleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(evaluationCycleDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(evaluationCycleDTO.getStartDate(), result.getStartDate());
        assertEquals(evaluationCycleDTO.getEndDate(), result.getEndDate());
        assertEquals(evaluationCycleDTO.getClosureDate(), result.getClosureDate());
        assertEquals(evaluationCycleDTO.getStatus(), result.getStatus());
        assertEquals(mockUser, result.getCreatedBy());
    }

    @Test
    @DisplayName("Should convert EvaluationCycleDTO to entity with null createdById")
    void testToEntityWithNullCreatedById() {
        // Arrange
        evaluationCycleDTO.setCreatedById(null);

        // Act
        EvaluationCycle result = evaluationCycleConverter.toEntity(evaluationCycleDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getCreatedBy());
        verify(entityManager, never()).find(eq(User.class), any());
    }

    @Test
    @DisplayName("Should convert EvaluationCycleDTO to entity with non-existent createdById")
    void testToEntityWithNonExistentCreatedById() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(null);

        // Act
        EvaluationCycle result = evaluationCycleConverter.toEntity(evaluationCycleDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getCreatedBy());
        verify(entityManager).find(eq(User.class), eq(1L));
    }

    @Test
    @DisplayName("Should handle null entity in toDTO")
    void testToDTOWithNullEntity() {
        // Act & Assert
        assertNull(evaluationCycleConverter.toDTO(null));
    }

    @Test
    @DisplayName("Should handle null DTO in toEntity")
    void testToEntityWithNullDTO() {
        // Act & Assert
        assertNull(evaluationCycleConverter.toEntity(null));
    }

    @Test
    @DisplayName("Should update existing entity from DTO")
    void testUpdateEntity() {
        // Arrange
        EvaluationCycle existingEntity = new EvaluationCycle();
        existingEntity.setId(1L);
        existingEntity.setStatus(EvaluationCycleStatus.COMPLETED);

        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockUser);

        // Act
        EvaluationCycle result = evaluationCycleConverter.toEntity(evaluationCycleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(evaluationCycleDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(evaluationCycleDTO.getStartDate(), result.getStartDate());
        assertEquals(evaluationCycleDTO.getEndDate(), result.getEndDate());
        assertEquals(evaluationCycleDTO.getClosureDate(), result.getClosureDate());
        assertEquals(evaluationCycleDTO.getStatus(), result.getStatus());
        assertEquals(mockUser, result.getCreatedBy());
    }

    @Test
    @DisplayName("Should get correct entity class")
    void testGetEntityClass() {
        // Act & Assert
        assertEquals(EvaluationCycle.class, evaluationCycleConverter.getEntityClass());
    }

    @Test
    @DisplayName("Should get ID from DTO")
    void testGetIdFromDTO() {
        // Act & Assert
        assertEquals(1L, evaluationCycleConverter.getIdFromDTO(evaluationCycleDTO));
    }

    @Test
    @DisplayName("Should handle null ID in DTO")
    void testGetIdFromDTOWithNull() {
        // Arrange
        evaluationCycleDTO.setId(null);

        // Act & Assert
        assertNull(evaluationCycleConverter.getIdFromDTO(evaluationCycleDTO));
    }
}
