package aor.proj3t.util.converterutils;

import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainingParticipationConverter Tests")
public class TrainingParticipationConverterTest extends BaseConverterTest<TrainingParticipation, TrainingParticipationDTO, TrainingParticipationConverter> {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TrainingConverter trainingConverter;

    @InjectMocks
    private TrainingParticipationConverter trainingParticipationConverter;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(TrainingParticipation.class), any(Long.class))).thenReturn(null);
        lenient().when(entityManager.find(eq(User.class), any(Long.class))).thenReturn(null);
        lenient().when(entityManager.find(eq(Training.class), any(Long.class))).thenReturn(null);
    }

    @Override
    protected TrainingParticipationConverter getConverter() {
        return trainingParticipationConverter;
    }

    @Override
    protected TrainingParticipation createValidEntity() {
        TrainingParticipation participation = new TrainingParticipation();
        participation.setId(1L);
        participation.setCreatedAt(Instant.now());
        participation.setParticipatedAt(Instant.now());
        return participation;
    }

    @Override
    protected TrainingParticipationDTO createValidDTO() {
        TrainingParticipationDTO dto = new TrainingParticipationDTO();
        dto.setId(1L);
        dto.setCreatedAt(Instant.now());
        dto.setParticipatedAt(Instant.now());
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(TrainingParticipation entity, TrainingParticipationDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getParticipatedAt(), dto.getParticipatedAt());
    }

    @Override
    protected void assertDTOEqualsEntity(TrainingParticipationDTO dto, TrainingParticipation entity) {
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getParticipatedAt(), entity.getParticipatedAt());
    }

    @Test
    @DisplayName("Should convert participation entity with participant to DTO")
    public void testParticipationWithParticipantToDTO() {
        // Given
        User participant = createValidUser();
        participant.setId(2L);
        participant.setEmail("participant@example.com");
        
        TrainingParticipation participation = createValidEntity();
        participation.setParticipant(participant);
        
        // When
        TrainingParticipationDTO dto = trainingParticipationConverter.toDTO(participation);
        
        // Then
        assertNotNull(dto);
        assertEquals(participant.getId(), dto.getParticipantId());
    }

    @Test
    @DisplayName("Should convert participation entity with manager to DTO")
    public void testParticipationWithManagerToDTO() {
        // Given
        User manager = createValidUser();
        manager.setId(3L);
        manager.setEmail("manager@example.com");
        manager.setRole(Role.MANAGER);
        
        TrainingParticipation participation = createValidEntity();
        participation.setManager(manager);
        
        // When
        TrainingParticipationDTO dto = trainingParticipationConverter.toDTO(participation);
        
        // Then
        assertNotNull(dto);
        assertEquals(manager.getId(), dto.getManagerId());
    }

    @Test
    @DisplayName("Should convert participation entity with training to DTO")
    public void testParticipationWithTrainingToDTO() {
        // Given
        Training training = createValidTraining();
        training.setId(4L);
        training.setTitle("Advanced Java");
        
        TrainingParticipation participation = createValidEntity();
        participation.setTraining(training);
        
        TrainingDTO trainingDTO = new TrainingDTO();
        trainingDTO.setId(4L);
        trainingDTO.setTitle("Advanced Java");
        
        when(trainingConverter.toDTO(training)).thenReturn(trainingDTO);
        
        // When
        TrainingParticipationDTO dto = trainingParticipationConverter.toDTO(participation);
        
        // Then
        assertNotNull(dto);
        assertNotNull(dto.getTraining());
        assertEquals(training.getId(), dto.getTraining().getId());
        assertEquals(training.getTitle(), dto.getTraining().getTitle());
        verify(trainingConverter).toDTO(training);
    }

    @Test
    @DisplayName("Should convert DTO with participant to entity")
    public void testDTOWithParticipantToEntity() {
        // Given
        TrainingParticipationDTO dto = createValidDTO();
        dto.setParticipantId(2L);
        
        User participant = createValidUser();
        participant.setId(2L);
        
        when(entityManager.find(User.class, 2L)).thenReturn(participant);
        
        // When
        TrainingParticipation entity = trainingParticipationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getParticipant());
        assertEquals(participant.getId(), entity.getParticipant().getId());
    }

    @Test
    @DisplayName("Should convert DTO with manager to entity")
    public void testDTOWithManagerToEntity() {
        // Given
        TrainingParticipationDTO dto = createValidDTO();
        dto.setManagerId(3L);
        
        User manager = createValidUser();
        manager.setId(3L);
        
        when(entityManager.find(User.class, 3L)).thenReturn(manager);
        
        // When
        TrainingParticipation entity = trainingParticipationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getManager());
        assertEquals(manager.getId(), entity.getManager().getId());
    }

    @Test
    @DisplayName("Should convert DTO with training to entity")
    public void testDTOWithTrainingToEntity() {
        // Given
        TrainingParticipationDTO dto = createValidDTO();
        TrainingDTO trainingDTO = new TrainingDTO();
        trainingDTO.setId(4L);
        dto.setTraining(trainingDTO);
        
        Training training = createValidTraining();
        training.setId(4L);
        
        when(entityManager.find(Training.class, 4L)).thenReturn(training);
        
        // When
        TrainingParticipation entity = trainingParticipationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getTraining());
        assertEquals(training.getId(), entity.getTraining().getId());
    }

    @Test
    @DisplayName("Should handle participation without relationships")
    public void testParticipationWithoutRelationships() {
        // Given
        TrainingParticipation participation = createValidEntity();
        participation.setParticipant(null);
        participation.setManager(null);
        participation.setTraining(null);
        
        // When
        TrainingParticipationDTO dto = trainingParticipationConverter.toDTO(participation);
        
        // Then
        assertNotNull(dto);
        assertNull(dto.getParticipantId());
        assertNull(dto.getManagerId());
        assertNull(dto.getTraining());
    }

    @Test
    @DisplayName("Should handle entities not found gracefully")
    public void testEntitiesNotFoundInEntity() {
        // Given
        TrainingParticipationDTO dto = createValidDTO();
        dto.setParticipantId(999L);
        dto.setManagerId(998L);
        
        TrainingDTO trainingDTO = new TrainingDTO();
        trainingDTO.setId(997L);
        dto.setTraining(trainingDTO);
        
        when(entityManager.find(User.class, 999L)).thenReturn(null);
        when(entityManager.find(User.class, 998L)).thenReturn(null);
        when(entityManager.find(Training.class, 997L)).thenReturn(null);
        
        // When
        TrainingParticipation entity = trainingParticipationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNull(entity.getParticipant());
        assertNull(entity.getManager());
        assertNull(entity.getTraining());
    }

    @Test
    @DisplayName("Should handle participation with null participant ID")
    public void testParticipationWithNullParticipantId() {
        // Given
        TrainingParticipationDTO dto = createValidDTO();
        dto.setParticipantId(null);
        
        // When
        TrainingParticipation entity = trainingParticipationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNull(entity.getParticipant());
    }

    @Test
    @DisplayName("Should handle participation with null manager ID")
    public void testParticipationWithNullManagerId() {
        // Given
        TrainingParticipationDTO dto = createValidDTO();
        dto.setManagerId(null);
        
        // When
        TrainingParticipation entity = trainingParticipationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNull(entity.getManager());
    }

    @Test
    @DisplayName("Should handle participation with null training")
    public void testParticipationWithNullTraining() {
        // Given
        TrainingParticipationDTO dto = createValidDTO();
        dto.setTraining(null);
        
        // When
        TrainingParticipation entity = trainingParticipationConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNull(entity.getTraining());
    }

    @Test
    @DisplayName("Should handle participation with future participated date")
    public void testParticipationWithFutureDate() {
        // Given
        TrainingParticipation participation = createValidEntity();
        Instant futureDate = Instant.now().plusSeconds(86400); // 1 day from now
        participation.setParticipatedAt(futureDate);
        
        // When
        TrainingParticipationDTO dto = trainingParticipationConverter.toDTO(participation);
        
        // Then
        assertNotNull(dto);
        assertEquals(futureDate, dto.getParticipatedAt());
    }

    @Test
    @DisplayName("Should handle participation with past participated date")
    public void testParticipationWithPastDate() {
        // Given
        TrainingParticipation participation = createValidEntity();
        Instant pastDate = Instant.now().minusSeconds(86400); // 1 day ago
        participation.setParticipatedAt(pastDate);
        
        // When
        TrainingParticipationDTO dto = trainingParticipationConverter.toDTO(participation);
        
        // Then
        assertNotNull(dto);
        assertEquals(pastDate, dto.getParticipatedAt());
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

    private Training createValidTraining() {
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
}
