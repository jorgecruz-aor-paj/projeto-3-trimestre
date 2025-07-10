package aor.proj3t.util.converterutils;

import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.Workplace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WorkplaceConverter Tests")
public class WorkplaceConverterTest extends BaseConverterTest<Workplace, WorkplaceDTO, WorkplaceConverter> {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private WorkplaceConverter workplaceConverter;

    @Override
    protected WorkplaceConverter getConverter() {
        return workplaceConverter;
    }

    @Override
    protected Workplace createValidEntity() {
        Workplace workplace = new Workplace();
        workplace.setId(1L);
        workplace.setName("Test Workplace");
        return workplace;
    }

    @Override
    protected WorkplaceDTO createValidDTO() {
        WorkplaceDTO dto = new WorkplaceDTO();
        dto.setId(1L);
        dto.setName("Test Workplace");
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(Workplace entity, WorkplaceDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Override
    protected void assertDTOEqualsEntity(WorkplaceDTO dto, Workplace entity) {
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    @DisplayName("Should handle workplace with minimal fields")
    public void testWorkplaceWithMinimalFields() {
        // Given
        Workplace workplace = new Workplace();
        workplace.setId(1L);
        workplace.setName("Minimal Workplace");
        
        // When
        WorkplaceDTO dto = workplaceConverter.toDTO(workplace);
        
        // Then
        assertNotNull(dto);
        assertEquals(workplace.getId(), dto.getId());
        assertEquals(workplace.getName(), dto.getName());
    }

    @Test
    @DisplayName("Should handle DTO with new ID (0) correctly")
    public void testDTOWithNewIdToEntity() {
        // Given
        WorkplaceDTO dto = createValidDTO();
        dto.setId(0L); // New entity
        
        // When
        Workplace entity = workplaceConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
    }
}
