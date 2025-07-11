package aor.proj3t.unit.util.converterutils;

import aor.proj3t.dto.SessionDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.entities.User;
import aor.proj3t.util.converterutils.SessionConverter;
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
 * Unit tests for SessionConverter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SessionConverter Tests")
public class SessionConverterTest extends BaseConverterTest<Session, SessionDTO, SessionConverter> {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SessionConverter sessionConverter;

    private Session session;
    private SessionDTO sessionDTO;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(User.class), any(Long.class))).thenReturn(null);
        
        // Create mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        // Create test session
        session = new Session();
        session.setId(1L);
        session.setCreatedAt(Instant.now());
        session.setExpirationDate(Instant.now().plusSeconds(3600));
        session.setSessionToken("test-token-123");
        session.setActive(true);
        session.setIpAddress("192.168.1.1");
        session.setUserAgent("Mozilla/5.0");
        session.setUser(mockUser);

        // Create test DTO
        sessionDTO = new SessionDTO();
        sessionDTO.setId(1L);
        sessionDTO.setCreatedAt(Instant.now());
        sessionDTO.setExpirationDate(Instant.now().plusSeconds(3600));
        sessionDTO.setSessionToken("test-token-123");
        sessionDTO.setActive(true);
        sessionDTO.setIpAddress("192.168.1.1");
        sessionDTO.setUserAgent("Mozilla/5.0");
        sessionDTO.setUserId(1L);
    }

    @Override
    protected SessionConverter getConverter() {
        return sessionConverter;
    }

    @Override
    protected Session createValidEntity() {
        Session entity = new Session();
        entity.setId(1L);
        entity.setCreatedAt(Instant.now());
        entity.setExpirationDate(Instant.now().plusSeconds(3600));
        entity.setSessionToken("test-token-123");
        entity.setActive(true);
        entity.setIpAddress("192.168.1.1");
        entity.setUserAgent("Mozilla/5.0");
        // Don't set user for base test
        return entity;
    }

    @Override
    protected SessionDTO createValidDTO() {
        SessionDTO dto = new SessionDTO();
        dto.setId(1L);
        dto.setCreatedAt(Instant.now());
        dto.setExpirationDate(Instant.now().plusSeconds(3600));
        dto.setSessionToken("test-token-123");
        dto.setActive(true);
        dto.setIpAddress("192.168.1.1");
        dto.setUserAgent("Mozilla/5.0");
        // Don't set userId for base test
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(Session entity, SessionDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getExpirationDate(), dto.getExpirationDate());
        assertEquals(entity.getSessionToken(), dto.getSessionToken());
        assertEquals(entity.getActive(), dto.getActive());
        assertEquals(entity.getIpAddress(), dto.getIpAddress());
        assertEquals(entity.getUserAgent(), dto.getUserAgent());
        // Note: Not checking user relationship in base test
    }

    @Override
    protected void assertDTOEqualsEntity(SessionDTO dto, Session entity) {
        // Don't check ID as it comes from database, not DTO
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getExpirationDate(), entity.getExpirationDate());
        assertEquals(dto.getSessionToken(), entity.getSessionToken());
        assertEquals(dto.getActive(), entity.getActive());
        assertEquals(dto.getIpAddress(), entity.getIpAddress());
        assertEquals(dto.getUserAgent(), entity.getUserAgent());
        // Note: Not checking user relationship in base test
    }

    @Test
    @DisplayName("Should convert Session entity to DTO")
    void testToDTO() {
        // Act
        SessionDTO result = sessionConverter.toDTO(session);

        // Assert
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getCreatedAt(), result.getCreatedAt());
        assertEquals(session.getExpirationDate(), result.getExpirationDate());
        assertEquals(session.getSessionToken(), result.getSessionToken());
        assertEquals(session.getActive(), result.getActive());
        assertEquals(session.getIpAddress(), result.getIpAddress());
        assertEquals(session.getUserAgent(), result.getUserAgent());
        assertEquals(session.getUser().getId(), result.getUserId());
    }

    @Test
    @DisplayName("Should convert Session entity to DTO with null user")
    void testToDTOWithNullUser() {
        // Arrange
        session.setUser(null);

        // Act
        SessionDTO result = sessionConverter.toDTO(session);

        // Assert
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertNull(result.getUserId());
    }

    @Test
    @DisplayName("Should convert SessionDTO to entity")
    void testToEntity() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(mockUser);

        // Act
        Session result = sessionConverter.toEntity(sessionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(sessionDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(sessionDTO.getExpirationDate(), result.getExpirationDate());
        assertEquals(sessionDTO.getSessionToken(), result.getSessionToken());
        assertEquals(sessionDTO.getActive(), result.getActive());
        assertEquals(sessionDTO.getIpAddress(), result.getIpAddress());
        assertEquals(sessionDTO.getUserAgent(), result.getUserAgent());
        assertEquals(mockUser, result.getUser());
    }

    @Test
    @DisplayName("Should convert SessionDTO to entity with null userId")
    void testToEntityWithNullUserId() {
        // Arrange
        sessionDTO.setUserId(null);

        // Act
        Session result = sessionConverter.toEntity(sessionDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getUser());
        verify(entityManager, never()).find(eq(User.class), any());
    }

    @Test
    @DisplayName("Should convert SessionDTO to entity with non-existent userId")
    void testToEntityWithNonExistentUserId() {
        // Arrange
        lenient().when(entityManager.find(eq(User.class), eq(1L))).thenReturn(null);

        // Act
        Session result = sessionConverter.toEntity(sessionDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getUser());
        verify(entityManager).find(eq(User.class), eq(1L));
    }

    @Test
    @DisplayName("Should handle null entity in toDTO")
    void testToDTOWithNullEntity() {
        // Act & Assert
        assertNull(sessionConverter.toDTO(null));
    }

    @Test
    @DisplayName("Should handle null DTO in toEntity")
    void testToEntityWithNullDTO() {
        // Act & Assert
        assertNull(sessionConverter.toEntity(null));
    }

    @Test
    @DisplayName("Should get correct entity class")
    void testGetEntityClass() {
        // Act & Assert
        assertEquals(Session.class, sessionConverter.getEntityClass());
    }

    @Test
    @DisplayName("Should get ID from DTO")
    void testGetIdFromDTO() {
        // Act & Assert
        assertEquals(1L, sessionConverter.getIdFromDTO(sessionDTO));
    }

    @Test
    @DisplayName("Should handle null ID in DTO")
    void testGetIdFromDTOWithNull() {
        // Arrange
        sessionDTO.setId(null);

        // Act & Assert
        assertNull(sessionConverter.getIdFromDTO(sessionDTO));
    }
}
