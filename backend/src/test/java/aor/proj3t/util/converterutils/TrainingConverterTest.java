package aor.proj3t.util.converterutils;

import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.enums.TrainingLanguage;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainingConverter Tests")
public class TrainingConverterTest extends BaseConverterTest<Training, TrainingDTO, TrainingConverter> {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TrainingAreaConverter trainingAreaConverter;

    @InjectMocks
    private TrainingConverter trainingConverter;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(Training.class), any(Long.class))).thenReturn(null);
        lenient().when(entityManager.find(eq(TrainingArea.class), any(Long.class))).thenReturn(null);
    }

    @Override
    protected TrainingConverter getConverter() {
        return trainingConverter;
    }

    @Override
    protected Training createValidEntity() {
        Training training = new Training();
        training.setId(1L);
        training.setTitle("Java Fundamentals");
        training.setDuration(120);
        training.setDescription("Learn Java programming basics");
        training.setLink("https://example.com/java-course");
        training.setLanguage(TrainingLanguage.ENGLISH);
        training.setActive(true);
        training.setCreatedAt(Instant.now());
        training.setEditedAt(Instant.now());
        return training;
    }

    @Override
    protected TrainingDTO createValidDTO() {
        TrainingDTO dto = new TrainingDTO();
        dto.setId(1L);
        dto.setTitle("Java Fundamentals");
        dto.setDuration(120);
        dto.setDescription("Learn Java programming basics");
        dto.setLink("https://example.com/java-course");
        dto.setLanguage(TrainingLanguage.ENGLISH);
        dto.setActive(true);
        dto.setCreatedAt(Instant.now());
        dto.setEditedAt(Instant.now());
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(Training entity, TrainingDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getTitle(), dto.getTitle());
        assertEquals(entity.getDuration(), dto.getDuration());
        assertEquals(entity.getDescription(), dto.getDescription());
        assertEquals(entity.getLink(), dto.getLink());
        assertEquals(entity.getLanguage(), dto.getLanguage());
        assertEquals(entity.isActive(), dto.isActive());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getEditedAt(), dto.getEditedAt());
    }

    @Override
    protected void assertDTOEqualsEntity(TrainingDTO dto, Training entity) {
        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getDuration(), entity.getDuration());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getLink(), entity.getLink());
        assertEquals(dto.getLanguage(), entity.getLanguage());
        assertEquals(dto.isActive(), entity.isActive());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getEditedAt(), entity.getEditedAt());
    }

    @Test
    @DisplayName("Should convert training entity with training area to DTO")
    public void testTrainingWithTrainingAreaToDTO() {
        // Given
        TrainingArea trainingArea = new TrainingArea();
        trainingArea.setId(1L);
        trainingArea.setName("Programming");
        
        Training training = createValidEntity();
        training.setTrainingArea(trainingArea);
        
        TrainingAreaDTO trainingAreaDTO = new TrainingAreaDTO();
        trainingAreaDTO.setId(1L);
        trainingAreaDTO.setName("Programming");
        
        when(trainingAreaConverter.toDTO(trainingArea)).thenReturn(trainingAreaDTO);
        
        // When
        TrainingDTO dto = trainingConverter.toDTO(training);
        
        // Then
        assertNotNull(dto);
        assertNotNull(dto.getTrainingArea());
        assertEquals(trainingArea.getId(), dto.getTrainingArea().getId());
        assertEquals(trainingArea.getName(), dto.getTrainingArea().getName());
        verify(trainingAreaConverter).toDTO(trainingArea);
    }

    @Test
    @DisplayName("Should convert training entity without training area to DTO")
    public void testTrainingWithoutTrainingAreaToDTO() {
        // Given
        Training training = createValidEntity();
        training.setTrainingArea(null);
        
        // When
        TrainingDTO dto = trainingConverter.toDTO(training);
        
        // Then
        assertNotNull(dto);
        assertNull(dto.getTrainingArea());
    }

    @Test
    @DisplayName("Should convert DTO with training area to entity")
    public void testDTOWithTrainingAreaToEntity() {
        // Given
        TrainingDTO dto = createValidDTO();
        TrainingAreaDTO trainingAreaDTO = new TrainingAreaDTO();
        trainingAreaDTO.setId(1L);
        dto.setTrainingArea(trainingAreaDTO);
        
        TrainingArea trainingArea = new TrainingArea();
        trainingArea.setId(1L);
        
        when(entityManager.find(TrainingArea.class, 1L)).thenReturn(trainingArea);
        
        // When
        Training entity = trainingConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getTrainingArea());
        assertEquals(trainingArea.getId(), entity.getTrainingArea().getId());
    }

    @Test
    @DisplayName("Should handle training area not found gracefully")
    public void testTrainingAreaNotFoundInEntity() {
        // Given
        TrainingDTO dto = createValidDTO();
        TrainingAreaDTO trainingAreaDTO = new TrainingAreaDTO();
        trainingAreaDTO.setId(999L); // Non-existent training area
        dto.setTrainingArea(trainingAreaDTO);
        
        when(entityManager.find(TrainingArea.class, 999L)).thenReturn(null);
        
        // When
        Training entity = trainingConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNull(entity.getTrainingArea());
    }

    @Test
    @DisplayName("Should handle training with null language")
    public void testTrainingWithNullLanguage() {
        // Given
        Training training = createValidEntity();
        training.setLanguage(null);
        
        // When
        TrainingDTO dto = trainingConverter.toDTO(training);
        
        // Then
        assertNotNull(dto);
        assertNull(dto.getLanguage());
    }

    @Test
    @DisplayName("Should handle training with all languages")
    public void testTrainingWithAllLanguages() {
        for (TrainingLanguage language : TrainingLanguage.values()) {
            // Given
            Training training = createValidEntity();
            training.setLanguage(language);
            
            // When
            TrainingDTO dto = trainingConverter.toDTO(training);
            
            // Then
            assertNotNull(dto);
            assertEquals(language, dto.getLanguage());
        }
    }

    @Test
    @DisplayName("Should handle inactive training")
    public void testInactiveTraining() {
        // Given
        Training training = createValidEntity();
        training.setActive(false);
        
        // When
        TrainingDTO dto = trainingConverter.toDTO(training);
        
        // Then
        assertNotNull(dto);
        assertFalse(dto.isActive());
    }

    @Test
    @DisplayName("Should handle training with long description")
    public void testTrainingWithLongDescription() {
        // Given
        Training training = createValidEntity();
        String longDescription = "A".repeat(1000); // Max length according to entity
        training.setDescription(longDescription);
        
        // When
        TrainingDTO dto = trainingConverter.toDTO(training);
        
        // Then
        assertNotNull(dto);
        assertEquals(longDescription, dto.getDescription());
    }

    @Test
    @DisplayName("Should handle training with long link")
    public void testTrainingWithLongLink() {
        // Given
        Training training = createValidEntity();
        String longLink = "https://example.com/" + "a".repeat(480); // Max length according to entity
        training.setLink(longLink);
        
        // When
        TrainingDTO dto = trainingConverter.toDTO(training);
        
        // Then
        assertNotNull(dto);
        assertEquals(longLink, dto.getLink());
    }
}
