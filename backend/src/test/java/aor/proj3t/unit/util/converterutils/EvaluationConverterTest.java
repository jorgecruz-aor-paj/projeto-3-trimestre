package aor.proj3t.unit.util.converterutils;

import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.dto.MiniEvaluationCycleDTO;
import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.enums.Role;
import aor.proj3t.util.converterutils.EvaluationConverter;
import aor.proj3t.util.converterutils.MiniEvaluationCycleConverter;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EvaluationConverter Tests")
public class EvaluationConverterTest extends BaseConverterTest<Evaluation, EvaluationDTO, EvaluationConverter> {

    @Mock
    private EntityManager entityManager;

    @Mock
    private MiniEvaluationCycleConverter miniEvaluationCycleConverter;

    @InjectMocks
    private EvaluationConverter evaluationConverter;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(Evaluation.class), any(Long.class))).thenReturn(null);
        lenient().when(entityManager.find(eq(EvaluationCycle.class), any(Long.class))).thenReturn(null);
        lenient().when(entityManager.find(eq(User.class), any(Long.class))).thenReturn(null);
    }

    @Override
    protected EvaluationConverter getConverter() {
        return evaluationConverter;
    }

    @Override
    protected Evaluation createValidEntity() {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setScale(EvaluationScale.EXPECTED_CONTRIBUTION);
        evaluation.setStatus(EvaluationStatus.COMPLETED);
        evaluation.setFeedback("Great performance this cycle");
        evaluation.setCreatedAt(Instant.now());
        evaluation.setEditedAt(Instant.now());
        return evaluation;
    }

    @Override
    protected EvaluationDTO createValidDTO() {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setId(1L);
        dto.setScale(EvaluationScale.EXPECTED_CONTRIBUTION);
        dto.setStatus(EvaluationStatus.COMPLETED);
        dto.setFeedback("Great performance this cycle");
        dto.setCreatedAt(Instant.now());
        dto.setEditedAt(Instant.now());
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(Evaluation entity, EvaluationDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getScale(), dto.getScale());
        assertEquals(entity.getStatus(), dto.getStatus());
        assertEquals(entity.getFeedback(), dto.getFeedback());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getEditedAt(), dto.getEditedAt());
    }

    @Override
    protected void assertDTOEqualsEntity(EvaluationDTO dto, Evaluation entity) {
        assertEquals(dto.getScale(), entity.getScale());
        assertEquals(dto.getStatus(), entity.getStatus());
        assertEquals(dto.getFeedback(), entity.getFeedback());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getEditedAt(), entity.getEditedAt());
    }

    @Test
    @DisplayName("Should convert evaluation entity with evaluation cycle to DTO")
    public void testEvaluationWithEvaluationCycleToDTO() {
        // Given
        EvaluationCycle evaluationCycle = new EvaluationCycle();
        evaluationCycle.setId(1L);
        
        Evaluation evaluation = createValidEntity();
        evaluation.setEvaluationCycle(evaluationCycle);
        
        MiniEvaluationCycleDTO evaluationCycleDTO = new MiniEvaluationCycleDTO();
        evaluationCycleDTO.setId(1L);
        
        when(miniEvaluationCycleConverter.toMiniEvaluationCycleDTO(evaluationCycle)).thenReturn(evaluationCycleDTO);
        
        // When
        EvaluationDTO dto = evaluationConverter.toDTO(evaluation);
        
        // Then
        assertNotNull(dto);
        assertNotNull(dto.getEvaluationCycle());
        assertEquals(evaluationCycle.getId(), dto.getEvaluationCycle().getId());
        verify(miniEvaluationCycleConverter).toMiniEvaluationCycleDTO(evaluationCycle);
    }

    @Test
    @DisplayName("Should convert evaluation entity with evaluator to DTO")
    public void testEvaluationWithEvaluatorToDTO() {
        // Given
        User evaluator = createValidUser();
        evaluator.setId(2L);
        evaluator.setEmail("evaluator@example.com");
        evaluator.setFirstName("Jane");
        evaluator.setLastName("Manager");
        
        Evaluation evaluation = createValidEntity();
        evaluation.setEvaluator(evaluator);
        
        // When
        EvaluationDTO dto = evaluationConverter.toDTO(evaluation);
        
        // Then
        assertNotNull(dto);
        assertNotNull(dto.getEvaluator());
        assertEquals(evaluator.getId(), dto.getEvaluator().getId());
        assertEquals(evaluator.getFirstName(), dto.getEvaluator().getFirstName());
        assertEquals(evaluator.getLastName(), dto.getEvaluator().getLastName());
        assertEquals(evaluator.getEmail(), dto.getEvaluator().getEmail());
        assertEquals(evaluator.getPhotograph(), dto.getEvaluator().getPhotograph());
    }

    @Test
    @DisplayName("Should convert evaluation entity with evaluatee to DTO")
    public void testEvaluationWithEvaluateeToDTO() {
        // Given
        User evaluatee = createValidUser();
        evaluatee.setId(3L);
        evaluatee.setEmail("evaluatee@example.com");
        evaluatee.setFirstName("John");
        evaluatee.setLastName("Employee");
        
        Evaluation evaluation = createValidEntity();
        evaluation.setEvaluated(evaluatee);
        
        // When
        EvaluationDTO dto = evaluationConverter.toDTO(evaluation);
        
        // Then
        assertNotNull(dto);
        assertNotNull(dto.getEvaluatee());
        assertEquals(evaluatee.getId(), dto.getEvaluatee().getId());
        assertEquals(evaluatee.getFirstName(), dto.getEvaluatee().getFirstName());
        assertEquals(evaluatee.getLastName(), dto.getEvaluatee().getLastName());
        assertEquals(evaluatee.getEmail(), dto.getEvaluatee().getEmail());
        assertEquals(evaluatee.getPhotograph(), dto.getEvaluatee().getPhotograph());
    }

    @Test
    @DisplayName("Should convert DTO with evaluation cycle to entity")
    public void testDTOWithEvaluationCycleToEntity() {
        // Given
        EvaluationDTO dto = createValidDTO();
        MiniEvaluationCycleDTO evaluationCycleDTO = new MiniEvaluationCycleDTO();
        evaluationCycleDTO.setId(1L);
        dto.setEvaluationCycle(evaluationCycleDTO);
        
        EvaluationCycle evaluationCycle = new EvaluationCycle();
        evaluationCycle.setId(1L);
        
        when(entityManager.find(EvaluationCycle.class, 1L)).thenReturn(evaluationCycle);
        
        // When
        Evaluation entity = evaluationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getEvaluationCycle());
        assertEquals(evaluationCycle.getId(), entity.getEvaluationCycle().getId());
    }

    @Test
    @DisplayName("Should convert DTO with evaluator to entity")
    public void testDTOWithEvaluatorToEntity() {
        // Given
        EvaluationDTO dto = createValidDTO();
        MiniUserDTO evaluatorDTO = new MiniUserDTO();
        evaluatorDTO.setId(2L);
        dto.setEvaluator(evaluatorDTO);
        
        User evaluator = createValidUser();
        evaluator.setId(2L);
        
        when(entityManager.find(User.class, 2L)).thenReturn(evaluator);
        
        // When
        Evaluation entity = evaluationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getEvaluator());
        assertEquals(evaluator.getId(), entity.getEvaluator().getId());
    }

    @Test
    @DisplayName("Should convert DTO with evaluatee to entity")
    public void testDTOWithEvaluateeToEntity() {
        // Given
        EvaluationDTO dto = createValidDTO();
        MiniUserDTO evaluateeDTO = new MiniUserDTO();
        evaluateeDTO.setId(3L);
        dto.setEvaluatee(evaluateeDTO);
        
        User evaluatee = createValidUser();
        evaluatee.setId(3L);
        
        when(entityManager.find(User.class, 3L)).thenReturn(evaluatee);
        
        // When
        Evaluation entity = evaluationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getEvaluated());
        assertEquals(evaluatee.getId(), entity.getEvaluated().getId());
    }

    @Test
    @DisplayName("Should handle all evaluation scales")
    public void testAllEvaluationScales() {
        for (EvaluationScale scale : EvaluationScale.values()) {
            // Given
            Evaluation evaluation = createValidEntity();
            evaluation.setScale(scale);
            
            // When
            EvaluationDTO dto = evaluationConverter.toDTO(evaluation);
            
            // Then
            assertNotNull(dto);
            assertEquals(scale, dto.getScale());
        }
    }

    @Test
    @DisplayName("Should handle all evaluation statuses")
    public void testAllEvaluationStatuses() {
        for (EvaluationStatus status : EvaluationStatus.values()) {
            // Given
            Evaluation evaluation = createValidEntity();
            evaluation.setStatus(status);
            
            // When
            EvaluationDTO dto = evaluationConverter.toDTO(evaluation);
            
            // Then
            assertNotNull(dto);
            assertEquals(status, dto.getStatus());
        }
    }

    @Test
    @DisplayName("Should handle evaluation with null feedback")
    public void testEvaluationWithNullFeedback() {
        // Given
        Evaluation evaluation = createValidEntity();
        evaluation.setFeedback(null);
        
        // When
        EvaluationDTO dto = evaluationConverter.toDTO(evaluation);
        
        // Then
        assertNotNull(dto);
        assertNull(dto.getFeedback());
    }

    @Test
    @DisplayName("Should handle evaluation with null scale")
    public void testEvaluationWithNullScale() {
        // Given
        Evaluation evaluation = createValidEntity();
        evaluation.setScale(null);
        
        // When
        EvaluationDTO dto = evaluationConverter.toDTO(evaluation);
        
        // Then
        assertNotNull(dto);
        assertNull(dto.getScale());
    }

    @Test
    @DisplayName("Should handle evaluation without relationships")
    public void testEvaluationWithoutRelationships() {
        // Given
        Evaluation evaluation = createValidEntity();
        evaluation.setEvaluationCycle(null);
        evaluation.setEvaluator(null);
        evaluation.setEvaluated(null);
        
        // When
        EvaluationDTO dto = evaluationConverter.toDTO(evaluation);
        
        // Then
        assertNotNull(dto);
        assertNull(dto.getEvaluationCycle());
        assertNull(dto.getEvaluator());
        assertNull(dto.getEvaluatee());
    }

    @Test
    @DisplayName("Should handle entities not found gracefully")
    public void testEntitiesNotFoundInEntity() {
        // Given
        EvaluationDTO dto = createValidDTO();
        
        MiniEvaluationCycleDTO evaluationCycleDTO = new MiniEvaluationCycleDTO();
        evaluationCycleDTO.setId(999L);
        dto.setEvaluationCycle(evaluationCycleDTO);
        
        MiniUserDTO evaluatorDTO = new MiniUserDTO();
        evaluatorDTO.setId(998L);
        dto.setEvaluator(evaluatorDTO);
        
        MiniUserDTO evaluateeDTO = new MiniUserDTO();
        evaluateeDTO.setId(997L);
        dto.setEvaluatee(evaluateeDTO);
        
        when(entityManager.find(EvaluationCycle.class, 999L)).thenReturn(null);
        when(entityManager.find(User.class, 998L)).thenReturn(null);
        when(entityManager.find(User.class, 997L)).thenReturn(null);
        
        // When
        Evaluation entity = evaluationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNull(entity.getEvaluationCycle());
        assertNull(entity.getEvaluator());
        assertNull(entity.getEvaluated());
    }

    private User createValidUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setPhotograph("profile.jpg");
        return user;
    }
}
