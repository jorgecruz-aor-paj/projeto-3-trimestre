package aor.proj3t.util.converterutils;

import aor.proj3t.dto.PendingRegistrationDTO;
import aor.proj3t.entities.PendingRegistration;
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

/**
 * Unit tests for PendingRegistrationConverter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PendingRegistrationConverter Tests")
public class PendingRegistrationConverterTest extends BaseConverterTest<PendingRegistration, PendingRegistrationDTO, PendingRegistrationConverter> {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PendingRegistrationConverter pendingRegistrationConverter;

    private PendingRegistration pendingRegistration;
    private PendingRegistrationDTO pendingRegistrationDTO;

    @BeforeEach
    void setUp() {
        // Create test pending registration
        pendingRegistration = new PendingRegistration();
        pendingRegistration.setId(1L);
        pendingRegistration.setEmail("test@example.com");
        pendingRegistration.setCreatedAt(Instant.now());
        pendingRegistration.setConfirmationToken("test-token-123");
        pendingRegistration.setExpirationDate(Instant.now().plusSeconds(86400));

        // Create test DTO
        pendingRegistrationDTO = new PendingRegistrationDTO();
        pendingRegistrationDTO.setId(1L);
        pendingRegistrationDTO.setEmail("test@example.com");
        pendingRegistrationDTO.setCreatedAt(Instant.now());
        pendingRegistrationDTO.setConfirmationToken("test-token-123");
        pendingRegistrationDTO.setExpirationDate(Instant.now().plusSeconds(86400));
    }

    @Override
    protected PendingRegistrationConverter getConverter() {
        return pendingRegistrationConverter;
    }

    @Override
    protected PendingRegistration createValidEntity() {
        PendingRegistration entity = new PendingRegistration();
        entity.setId(1L);
        entity.setEmail("test@example.com");
        entity.setCreatedAt(Instant.now());
        entity.setConfirmationToken("test-token-123");
        entity.setExpirationDate(Instant.now().plusSeconds(86400));
        return entity;
    }

    @Override
    protected PendingRegistrationDTO createValidDTO() {
        PendingRegistrationDTO dto = new PendingRegistrationDTO();
        // Don't set ID for DTO as it uses primitive long that defaults to 0
        dto.setEmail("test@example.com");
        dto.setCreatedAt(Instant.now());
        dto.setConfirmationToken("test-token-123");
        dto.setExpirationDate(Instant.now().plusSeconds(86400));
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(PendingRegistration entity, PendingRegistrationDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getConfirmationToken(), dto.getConfirmationToken());
        assertEquals(entity.getExpirationDate(), dto.getExpirationDate());
    }

    @Override
    protected void assertDTOEqualsEntity(PendingRegistrationDTO dto, PendingRegistration entity) {
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getConfirmationToken(), entity.getConfirmationToken());
        assertEquals(dto.getExpirationDate(), entity.getExpirationDate());
    }

    @Test
    @DisplayName("Should convert PendingRegistration entity to DTO")
    void testToDTO() {
        // Act
        PendingRegistrationDTO result = pendingRegistrationConverter.toDTO(pendingRegistration);

        // Assert
        assertNotNull(result);
        assertEquals(pendingRegistration.getId(), result.getId());
        assertEquals(pendingRegistration.getEmail(), result.getEmail());
        assertEquals(pendingRegistration.getCreatedAt(), result.getCreatedAt());
        assertEquals(pendingRegistration.getConfirmationToken(), result.getConfirmationToken());
        assertEquals(pendingRegistration.getExpirationDate(), result.getExpirationDate());
    }

    @Test
    @DisplayName("Should convert PendingRegistrationDTO to entity")
    void testToEntity() {
        // Act
        PendingRegistration result = pendingRegistrationConverter.toEntity(pendingRegistrationDTO);

        // Assert
        assertNotNull(result);
        assertEquals(pendingRegistrationDTO.getEmail(), result.getEmail());
        assertEquals(pendingRegistrationDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(pendingRegistrationDTO.getConfirmationToken(), result.getConfirmationToken());
        assertEquals(pendingRegistrationDTO.getExpirationDate(), result.getExpirationDate());
    }

    @Test
    @DisplayName("Should handle null entity in toDTO")
    void testToDTOWithNullEntity() {
        // Act & Assert
        assertNull(pendingRegistrationConverter.toDTO(null));
    }

    @Test
    @DisplayName("Should handle null DTO in toEntity")
    void testToEntityWithNullDTO() {
        // Act & Assert
        assertNull(pendingRegistrationConverter.toEntity(null));
    }

    @Test
    @DisplayName("Should get correct entity class")
    void testGetEntityClass() {
        // Act & Assert
        assertEquals(PendingRegistration.class, pendingRegistrationConverter.getEntityClass());
    }

    @Test
    @DisplayName("Should get ID from DTO")
    void testGetIdFromDTO() {
        // Act & Assert
        assertEquals(1L, pendingRegistrationConverter.getIdFromDTO(pendingRegistrationDTO));
    }

    @Test
    @DisplayName("Should handle zero ID in DTO")
    void testGetIdFromDTOWithZero() {
        // Arrange
        pendingRegistrationDTO.setId(0L);

        // Act & Assert
        assertNull(pendingRegistrationConverter.getIdFromDTO(pendingRegistrationDTO));
    }

    @Test
    @DisplayName("Should handle default ID in DTO")
    void testGetIdFromDTOWithDefault() {
        // Arrange - create DTO with default ID (0)
        PendingRegistrationDTO dtoWithDefaultId = new PendingRegistrationDTO();

        // Act & Assert
        assertNull(pendingRegistrationConverter.getIdFromDTO(dtoWithDefaultId));
    }
}
