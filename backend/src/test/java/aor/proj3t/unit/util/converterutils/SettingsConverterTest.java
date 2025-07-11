package aor.proj3t.unit.util.converterutils;

import aor.proj3t.dto.SettingsDTO;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.util.converterutils.SettingsConverter;
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
 * Unit tests for SettingsConverter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SettingsConverter Tests")
public class SettingsConverterTest extends BaseConverterTest<Settings, SettingsDTO, SettingsConverter> {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SettingsConverter settingsConverter;

    private Settings settings;
    private SettingsDTO settingsDTO;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(User.class), any(Long.class))).thenReturn(null);
        
        // Create mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("admin@example.com");

        // Create test settings
        settings = new Settings();
        settings.setId(1L);
        settings.setCreatedAt(Instant.now());
        settings.setConfirmationExpirationTime(24);
        settings.setSessionExpirationTime(120);
        settings.setResetPasswordExpirationTime(1);
        settings.setCreatedBy(mockUser);

        // Create test DTO
        settingsDTO = new SettingsDTO();
        settingsDTO.setId(1L);
        settingsDTO.setCreatedAt(Instant.now());
        settingsDTO.setConfirmationWindow(24);
        settingsDTO.setSessionWindow(120);
        settingsDTO.setResetPasswordWindow(1);
        settingsDTO.setCreatedById(1L);
    }

    @Override
    protected SettingsConverter getConverter() {
        return settingsConverter;
    }

    @Override
    protected Settings createValidEntity() {
        Settings entity = new Settings();
        entity.setId(1L);
        entity.setCreatedAt(Instant.now());
        entity.setConfirmationExpirationTime(24);
        entity.setSessionExpirationTime(120);
        entity.setResetPasswordExpirationTime(1);
        // Don't set createdBy for base test
        return entity;
    }

    @Override
    protected SettingsDTO createValidDTO() {
        SettingsDTO dto = new SettingsDTO();
        dto.setId(1L);
        dto.setCreatedAt(Instant.now());
        dto.setConfirmationWindow(24);
        dto.setSessionWindow(120);
        dto.setResetPasswordWindow(1);
        // Don't set createdById for base test
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(Settings entity, SettingsDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getConfirmationExpirationTime(), dto.getConfirmationWindow());
        assertEquals(entity.getSessionExpirationTime(), dto.getSessionWindow());
        assertEquals(entity.getResetPasswordExpirationTime(), dto.getResetPasswordWindow());
        // Note: Not checking createdBy relationship in base test
    }

    @Override
    protected void assertDTOEqualsEntity(SettingsDTO dto, Settings entity) {
        // Don't check ID as it comes from database, not DTO
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getConfirmationWindow(), entity.getConfirmationExpirationTime());
        assertEquals(dto.getSessionWindow(), entity.getSessionExpirationTime());
        assertEquals(dto.getResetPasswordWindow(), entity.getResetPasswordExpirationTime());
        // Note: Not checking createdBy relationship in base test
    }

    @Test
    @DisplayName("Should convert Settings entity to DTO")
    void testToDTO() {
        // Act
        SettingsDTO result = settingsConverter.toDTO(settings);

        // Assert
        assertNotNull(result);
        assertEquals(settings.getId(), result.getId());
        assertEquals(settings.getCreatedAt(), result.getCreatedAt());
        assertEquals(settings.getConfirmationExpirationTime(), result.getConfirmationWindow());
        assertEquals(settings.getSessionExpirationTime(), result.getSessionWindow());
        assertEquals(settings.getResetPasswordExpirationTime(), result.getResetPasswordWindow());
        assertEquals(settings.getCreatedBy().getId(), result.getCreatedById());
    }

    @Test
    @DisplayName("Should convert Settings entity to DTO with null createdBy")
    void testToDTOWithNullCreatedBy() {
        // Arrange
        settings.setCreatedBy(null);

        // Act
        SettingsDTO result = settingsConverter.toDTO(settings);

        // Assert
        assertNotNull(result);
        assertEquals(settings.getId(), result.getId());
        assertNull(result.getCreatedById());
    }

    @Test
    @DisplayName("Should convert SettingsDTO to entity")
    void testToEntity() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockUser);

        // Act
        Settings result = settingsConverter.toEntity(settingsDTO);

        // Assert
        assertNotNull(result);
        assertEquals(settingsDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(settingsDTO.getConfirmationWindow(), result.getConfirmationExpirationTime());
        assertEquals(settingsDTO.getSessionWindow(), result.getSessionExpirationTime());
        assertEquals(settingsDTO.getResetPasswordWindow(), result.getResetPasswordExpirationTime());
        assertEquals(mockUser, result.getCreatedBy());
    }

    @Test
    @DisplayName("Should convert SettingsDTO to entity with null createdById")
    void testToEntityWithNullCreatedById() {
        // Arrange
        settingsDTO.setCreatedById(null);

        // Act
        Settings result = settingsConverter.toEntity(settingsDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getCreatedBy());
        verify(entityManager, never()).find(eq(User.class), any());
    }

    @Test
    @DisplayName("Should convert SettingsDTO to entity with non-existent createdById")
    void testToEntityWithNonExistentCreatedById() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(null);

        // Act
        Settings result = settingsConverter.toEntity(settingsDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getCreatedBy());
        verify(entityManager).find(eq(User.class), eq(1L));
    }

    @Test
    @DisplayName("Should handle null entity in toDTO")
    void testToDTOWithNullEntity() {
        // Act & Assert
        assertNull(settingsConverter.toDTO(null));
    }

    @Test
    @DisplayName("Should handle null DTO in toEntity")
    void testToEntityWithNullDTO() {
        // Act & Assert
        assertNull(settingsConverter.toEntity(null));
    }

    @Test
    @DisplayName("Should get correct entity class")
    void testGetEntityClass() {
        // Act & Assert
        assertEquals(Settings.class, settingsConverter.getEntityClass());
    }

    @Test
    @DisplayName("Should get ID from DTO")
    void testGetIdFromDTO() {
        // Act & Assert
        assertEquals(1L, settingsConverter.getIdFromDTO(settingsDTO));
    }

    @Test
    @DisplayName("Should handle null ID in DTO")
    void testGetIdFromDTOWithNull() {
        // Arrange
        settingsDTO.setId(null);

        // Act & Assert
        assertNull(settingsConverter.getIdFromDTO(settingsDTO));
    }
}
