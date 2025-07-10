package aor.proj3t.util.converterutils;

import aor.proj3t.dto.ResetPasswordDTO;
import aor.proj3t.entities.ResetPassword;
import aor.proj3t.entities.User;
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
 * Unit tests for ResetPasswordConverter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ResetPasswordConverter Tests")
public class ResetPasswordConverterTest extends BaseConverterTest<ResetPassword, ResetPasswordDTO, ResetPasswordConverter> {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ResetPasswordConverter resetPasswordConverter;

    private ResetPassword resetPassword;
    private ResetPasswordDTO resetPasswordDTO;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(User.class), any(Long.class))).thenReturn(null);
        
        // Create mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        // Create test reset password
        resetPassword = new ResetPassword();
        resetPassword.setId(1L);
        resetPassword.setCreatedAt(Instant.now());
        resetPassword.setExpirationDate(Instant.now().plusSeconds(3600));
        resetPassword.setResetPasswordToken("reset-token-123");
        resetPassword.setUser(mockUser);

        // Create test DTO
        resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setId(1L);
        resetPasswordDTO.setCreatedAt(Instant.now());
        resetPasswordDTO.setExpirationDate(Instant.now().plusSeconds(3600));
        resetPasswordDTO.setResetPasswordToken("reset-token-123");
        resetPasswordDTO.setUserId(1L);
    }

    @Override
    protected ResetPasswordConverter getConverter() {
        return resetPasswordConverter;
    }

    @Override
    protected ResetPassword createValidEntity() {
        ResetPassword entity = new ResetPassword();
        entity.setId(1L);
        entity.setCreatedAt(Instant.now());
        entity.setExpirationDate(Instant.now().plusSeconds(3600));
        entity.setResetPasswordToken("reset-token-123");
        // Don't set user for base test
        return entity;
    }

    @Override
    protected ResetPasswordDTO createValidDTO() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setId(1L);
        dto.setCreatedAt(Instant.now());
        dto.setExpirationDate(Instant.now().plusSeconds(3600));
        dto.setResetPasswordToken("reset-token-123");
        // Don't set userId for base test
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(ResetPassword entity, ResetPasswordDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getExpirationDate(), dto.getExpirationDate());
        assertEquals(entity.getResetPasswordToken(), dto.getResetPasswordToken());
        // Note: Not checking user relationship in base test
    }

    @Override
    protected void assertDTOEqualsEntity(ResetPasswordDTO dto, ResetPassword entity) {
        // Don't check ID as it comes from database, not DTO
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getExpirationDate(), entity.getExpirationDate());
        assertEquals(dto.getResetPasswordToken(), entity.getResetPasswordToken());
        // Note: Not checking user relationship in base test
    }

    @Test
    @DisplayName("Should convert ResetPassword entity to DTO")
    void testToDTO() {
        // Act
        ResetPasswordDTO result = resetPasswordConverter.toDTO(resetPassword);

        // Assert
        assertNotNull(result);
        assertEquals(resetPassword.getId(), result.getId());
        assertEquals(resetPassword.getCreatedAt(), result.getCreatedAt());
        assertEquals(resetPassword.getExpirationDate(), result.getExpirationDate());
        assertEquals(resetPassword.getResetPasswordToken(), result.getResetPasswordToken());
        assertEquals(resetPassword.getUser().getId(), result.getUserId());
    }

    @Test
    @DisplayName("Should convert ResetPassword entity to DTO with null user")
    void testToDTOWithNullUser() {
        // Arrange
        resetPassword.setUser(null);

        // Act
        ResetPasswordDTO result = resetPasswordConverter.toDTO(resetPassword);

        // Assert
        assertNotNull(result);
        assertEquals(resetPassword.getId(), result.getId());
        assertNull(result.getUserId());
    }

    @Test
    @DisplayName("Should convert ResetPasswordDTO to entity")
    void testToEntity() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockUser);

        // Act
        ResetPassword result = resetPasswordConverter.toEntity(resetPasswordDTO);

        // Assert
        assertNotNull(result);
        assertEquals(resetPasswordDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(resetPasswordDTO.getExpirationDate(), result.getExpirationDate());
        assertEquals(resetPasswordDTO.getResetPasswordToken(), result.getResetPasswordToken());
        assertEquals(mockUser, result.getUser());
    }

    @Test
    @DisplayName("Should convert ResetPasswordDTO to entity with null userId")
    void testToEntityWithNullUserId() {
        // Arrange
        resetPasswordDTO.setUserId(null);

        // Act
        ResetPassword result = resetPasswordConverter.toEntity(resetPasswordDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getUser());
        verify(entityManager, never()).find(eq(User.class), any());
    }

    @Test
    @DisplayName("Should convert ResetPasswordDTO to entity with non-existent userId")
    void testToEntityWithNonExistentUserId() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(null);

        // Act
        ResetPassword result = resetPasswordConverter.toEntity(resetPasswordDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getUser());
        verify(entityManager).find(eq(User.class), eq(1L));
    }

    @Test
    @DisplayName("Should handle null entity in toDTO")
    void testToDTOWithNullEntity() {
        // Act & Assert
        assertNull(resetPasswordConverter.toDTO(null));
    }

    @Test
    @DisplayName("Should handle null DTO in toEntity")
    void testToEntityWithNullDTO() {
        // Act & Assert
        assertNull(resetPasswordConverter.toEntity(null));
    }

    @Test
    @DisplayName("Should get correct entity class")
    void testGetEntityClass() {
        // Act & Assert
        assertEquals(ResetPassword.class, resetPasswordConverter.getEntityClass());
    }

    @Test
    @DisplayName("Should get ID from DTO")
    void testGetIdFromDTO() {
        // Act & Assert
        assertEquals(1L, resetPasswordConverter.getIdFromDTO(resetPasswordDTO));
    }

    @Test
    @DisplayName("Should handle null ID in DTO")
    void testGetIdFromDTOWithNull() {
        // Arrange
        resetPasswordDTO.setId(null);

        // Act & Assert
        assertNull(resetPasswordConverter.getIdFromDTO(resetPasswordDTO));
    }
}
