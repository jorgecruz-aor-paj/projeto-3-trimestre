package aor.proj3t.util.converterutils;

import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.entities.TrainingArea;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainingAreaConverter Tests")
public class TrainingAreaConverterTest extends BaseConverterTest<TrainingArea, TrainingAreaDTO, TrainingAreaConverter> {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TrainingAreaConverter trainingAreaConverter;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(TrainingArea.class), any(Long.class))).thenReturn(null);
    }

    @Override
    protected TrainingAreaConverter getConverter() {
        return trainingAreaConverter;
    }

    @Override
    protected TrainingArea createValidEntity() {
        TrainingArea trainingArea = new TrainingArea();
        trainingArea.setId(1L);
        trainingArea.setName("Programming");
        return trainingArea;
    }

    @Override
    protected TrainingAreaDTO createValidDTO() {
        TrainingAreaDTO dto = new TrainingAreaDTO();
        dto.setId(1L);
        dto.setName("Programming");
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(TrainingArea entity, TrainingAreaDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Override
    protected void assertDTOEqualsEntity(TrainingAreaDTO dto, TrainingArea entity) {
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    @DisplayName("Should handle training area with minimal data")
    public void testTrainingAreaWithMinimalData() {
        // Given
        TrainingArea trainingArea = new TrainingArea();
        trainingArea.setId(1L);
        trainingArea.setName("IT");
        
        // When
        TrainingAreaDTO dto = trainingAreaConverter.toDTO(trainingArea);
        
        // Then
        assertNotNull(dto);
        assertEquals(trainingArea.getId(), dto.getId());
        assertEquals(trainingArea.getName(), dto.getName());
    }

    @Test
    @DisplayName("Should handle DTO with new ID (null) correctly")
    public void testDTOWithNullIdToEntity() {
        // Given
        TrainingAreaDTO dto = createValidDTO();
        dto.setId(null); // New entity
        
        // When
        TrainingArea entity = trainingAreaConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    @DisplayName("Should handle DTO with existing ID correctly")
    public void testDTOWithExistingIdToEntity() {
        // Given
        TrainingAreaDTO dto = createValidDTO();
        dto.setId(1L); // Existing entity
        
        TrainingArea existingArea = createValidEntity();
        when(entityManager.find(TrainingArea.class, 1L)).thenReturn(existingArea);
        
        // When
        TrainingArea entity = trainingAreaConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        verify(entityManager).find(TrainingArea.class, 1L);
    }

    @Test
    @DisplayName("Should handle various training area names")
    public void testVariousTrainingAreaNames() {
        String[] names = {
            "Information Technology",
            "Human Resources",
            "Sales & Marketing",
            "Finance",
            "Operations",
            "R&D",
            "Quality Assurance",
            "Customer Support"
        };

        for (String name : names) {
            // Given
            TrainingArea trainingArea = createValidEntity();
            trainingArea.setName(name);
            
            // When
            TrainingAreaDTO dto = trainingAreaConverter.toDTO(trainingArea);
            
            // Then
            assertNotNull(dto);
            assertEquals(name, dto.getName());
        }
    }

    @Test
    @DisplayName("Should handle training area with empty name")
    public void testTrainingAreaWithEmptyName() {
        // Given
        TrainingArea trainingArea = createValidEntity();
        trainingArea.setName("");
        
        // When
        TrainingAreaDTO dto = trainingAreaConverter.toDTO(trainingArea);
        
        // Then
        assertNotNull(dto);
        assertEquals("", dto.getName());
    }

    @Test
    @DisplayName("Should handle training area with whitespace name")
    public void testTrainingAreaWithWhitespaceName() {
        // Given
        TrainingArea trainingArea = createValidEntity();
        trainingArea.setName("   Programming   ");
        
        // When
        TrainingAreaDTO dto = trainingAreaConverter.toDTO(trainingArea);
        
        // Then
        assertNotNull(dto);
        assertEquals("   Programming   ", dto.getName());
    }

    @Test
    @DisplayName("Should handle training area with special characters")
    public void testTrainingAreaWithSpecialCharacters() {
        // Given
        TrainingArea trainingArea = createValidEntity();
        trainingArea.setName("AI & Machine Learning");
        
        // When
        TrainingAreaDTO dto = trainingAreaConverter.toDTO(trainingArea);
        
        // Then
        assertNotNull(dto);
        assertEquals("AI & Machine Learning", dto.getName());
    }

    @Test
    @DisplayName("Should handle training area with long name")
    public void testTrainingAreaWithLongName() {
        // Given
        TrainingArea trainingArea = createValidEntity();
        String longName = "Very Long Training Area Name That Could Be Used For Complex Departmental Training Programs";
        trainingArea.setName(longName);
        
        // When
        TrainingAreaDTO dto = trainingAreaConverter.toDTO(trainingArea);
        
        // Then
        assertNotNull(dto);
        assertEquals(longName, dto.getName());
    }

    @Test
    @DisplayName("Should handle training area with numeric name")
    public void testTrainingAreaWithNumericName() {
        // Given
        TrainingArea trainingArea = createValidEntity();
        trainingArea.setName("Area 51");
        
        // When
        TrainingAreaDTO dto = trainingAreaConverter.toDTO(trainingArea);
        
        // Then
        assertNotNull(dto);
        assertEquals("Area 51", dto.getName());
    }
}
