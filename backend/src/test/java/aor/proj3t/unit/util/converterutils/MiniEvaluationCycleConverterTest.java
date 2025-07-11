package aor.proj3t.unit.util.converterutils;

import aor.proj3t.dto.MiniEvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.util.converterutils.MiniEvaluationCycleConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MiniEvaluationCycleConverter Tests")
public class MiniEvaluationCycleConverterTest {

    private MiniEvaluationCycleConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new MiniEvaluationCycleConverter();
    }

    @Test
    @DisplayName("Should convert EvaluationCycle to MiniEvaluationCycleDTO successfully")
    public void testEvaluationCycleToMiniEvaluationCycleDTO() {
        // Given
        EvaluationCycle evaluationCycle = createValidEvaluationCycle();
        
        // When
        MiniEvaluationCycleDTO dto = converter.toMiniEvaluationCycleDTO(evaluationCycle);
        
        // Then
        assertNotNull(dto);
        assertEquals(evaluationCycle.getId(), dto.getId());
        assertEquals(evaluationCycle.getStartDate(), dto.getStartDate());
        assertEquals(evaluationCycle.getClosureDate(), dto.getClosureDate());
    }

    @Test
    @DisplayName("Should handle null evaluation cycle conversion gracefully")
    public void testNullEvaluationCycleToMiniEvaluationCycleDTO() {
        // When
        MiniEvaluationCycleDTO dto = converter.toMiniEvaluationCycleDTO(null);
        
        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Should convert evaluation cycle list to MiniEvaluationCycleDTO list successfully")
    public void testEvaluationCycleListToMiniEvaluationCycleDTOList() {
        // Given
        EvaluationCycle cycle1 = createValidEvaluationCycle();
        cycle1.setId(1L);
        
        EvaluationCycle cycle2 = createValidEvaluationCycle();
        cycle2.setId(2L);
        cycle2.setStartDate(Instant.now().plusSeconds(86400));
        cycle2.setClosureDate(Instant.now().plusSeconds(172800));
        
        List<EvaluationCycle> cycleList = Arrays.asList(cycle1, cycle2);
        
        // When
        List<MiniEvaluationCycleDTO> dtoList = converter.toMiniEvaluationCycleDTOList(cycleList);
        
        // Then
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        
        MiniEvaluationCycleDTO dto1 = dtoList.get(0);
        assertNotNull(dto1);
        assertEquals(cycle1.getId(), dto1.getId());
        assertEquals(cycle1.getStartDate(), dto1.getStartDate());
        assertEquals(cycle1.getClosureDate(), dto1.getClosureDate());
        
        MiniEvaluationCycleDTO dto2 = dtoList.get(1);
        assertNotNull(dto2);
        assertEquals(cycle2.getId(), dto2.getId());
        assertEquals(cycle2.getStartDate(), dto2.getStartDate());
        assertEquals(cycle2.getClosureDate(), dto2.getClosureDate());
    }

    @Test
    @DisplayName("Should handle empty evaluation cycle list conversion")
    public void testEmptyEvaluationCycleListToMiniEvaluationCycleDTOList() {
        // Given
        List<EvaluationCycle> cycleList = Arrays.asList();
        
        // When
        List<MiniEvaluationCycleDTO> dtoList = converter.toMiniEvaluationCycleDTOList(cycleList);
        
        // Then
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    @DisplayName("Should handle evaluation cycle with null dates")
    public void testEvaluationCycleWithNullDates() {
        // Given
        EvaluationCycle evaluationCycle = createValidEvaluationCycle();
        evaluationCycle.setStartDate(null);
        evaluationCycle.setClosureDate(null);
        
        // When
        MiniEvaluationCycleDTO dto = converter.toMiniEvaluationCycleDTO(evaluationCycle);
        
        // Then
        assertNotNull(dto);
        assertEquals(evaluationCycle.getId(), dto.getId());
        assertNull(dto.getStartDate());
        assertNull(dto.getClosureDate());
    }

    @Test
    @DisplayName("Should handle evaluation cycle with only start date")
    public void testEvaluationCycleWithOnlyStartDate() {
        // Given
        EvaluationCycle evaluationCycle = createValidEvaluationCycle();
        evaluationCycle.setClosureDate(null);
        
        // When
        MiniEvaluationCycleDTO dto = converter.toMiniEvaluationCycleDTO(evaluationCycle);
        
        // Then
        assertNotNull(dto);
        assertEquals(evaluationCycle.getId(), dto.getId());
        assertEquals(evaluationCycle.getStartDate(), dto.getStartDate());
        assertNull(dto.getClosureDate());
    }

    @Test
    @DisplayName("Should handle evaluation cycle with only closure date")
    public void testEvaluationCycleWithOnlyClosureDate() {
        // Given
        EvaluationCycle evaluationCycle = createValidEvaluationCycle();
        evaluationCycle.setStartDate(null);
        
        // When
        MiniEvaluationCycleDTO dto = converter.toMiniEvaluationCycleDTO(evaluationCycle);
        
        // Then
        assertNotNull(dto);
        assertEquals(evaluationCycle.getId(), dto.getId());
        assertNull(dto.getStartDate());
        assertEquals(evaluationCycle.getClosureDate(), dto.getClosureDate());
    }

    @Test
    @DisplayName("Should handle evaluation cycle with past dates")
    public void testEvaluationCycleWithPastDates() {
        // Given
        EvaluationCycle evaluationCycle = createValidEvaluationCycle();
        Instant pastStart = Instant.now().minusSeconds(172800); // 2 days ago
        Instant pastEnd = Instant.now().minusSeconds(86400); // 1 day ago
        evaluationCycle.setStartDate(pastStart);
        evaluationCycle.setClosureDate(pastEnd);
        
        // When
        MiniEvaluationCycleDTO dto = converter.toMiniEvaluationCycleDTO(evaluationCycle);
        
        // Then
        assertNotNull(dto);
        assertEquals(evaluationCycle.getId(), dto.getId());
        assertEquals(pastStart, dto.getStartDate());
        assertEquals(pastEnd, dto.getClosureDate());
    }

    @Test
    @DisplayName("Should handle evaluation cycle with future dates")
    public void testEvaluationCycleWithFutureDates() {
        // Given
        EvaluationCycle evaluationCycle = createValidEvaluationCycle();
        Instant futureStart = Instant.now().plusSeconds(86400); // 1 day from now
        Instant futureEnd = Instant.now().plusSeconds(172800); // 2 days from now
        evaluationCycle.setStartDate(futureStart);
        evaluationCycle.setClosureDate(futureEnd);
        
        // When
        MiniEvaluationCycleDTO dto = converter.toMiniEvaluationCycleDTO(evaluationCycle);
        
        // Then
        assertNotNull(dto);
        assertEquals(evaluationCycle.getId(), dto.getId());
        assertEquals(futureStart, dto.getStartDate());
        assertEquals(futureEnd, dto.getClosureDate());
    }

    private EvaluationCycle createValidEvaluationCycle() {
        EvaluationCycle cycle = new EvaluationCycle();
        cycle.setId(1L);
        cycle.setStartDate(Instant.now());
        cycle.setClosureDate(Instant.now().plusSeconds(86400)); // 1 day later
        cycle.setStatus(EvaluationCycleStatus.ACTIVE);
        cycle.setCreatedAt(Instant.now());
        return cycle;
    }
}
