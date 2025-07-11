package aor.proj3t.unit.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.SessionDAO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.impl.TokenServiceImpl;
import aor.proj3t.service.interfaces.UserService;
import aor.proj3t.util.authutils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private SessionDAO sessionDAO;

    @Mock
    private UserService userService;

    @Mock
    private Claims claims;

    @InjectMocks
    private TokenServiceImpl tokenService;

    // Test data
    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String VALID_AUTH_HEADER = "Bearer " + VALID_TOKEN;
    private static final Long USER_ID = 1L;
    private static final String ROLE_STRING = "ADMIN";
    private static final Role USER_ROLE = Role.ADMIN;

    @BeforeEach
    void setUp() {
        lenient().when(daoFactory.forSession()).thenReturn(sessionDAO);
        lenient().when(serviceFactory.forUser()).thenReturn(userService);
    }

    @Test
    @DisplayName("getUserIdFromToken should return userId when token is valid")
    void getUserIdFromToken_WithValidToken_ShouldReturnUserId() {
        // Given
        Session activeSession = new Session();
        activeSession.setActive(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(USER_ID);

        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(activeSession));
        when(jwtUtil.extractAllClaims(VALID_TOKEN)).thenReturn(claims);
        when(claims.get("userId")).thenReturn(USER_ID);
        when(userService.getUser(USER_ID)).thenReturn(userDTO);

        // When
        Long result = tokenService.getUserIdFromToken(VALID_TOKEN);

        // Then
        assertEquals(USER_ID, result);
        verify(jwtUtil).validateToken(VALID_TOKEN);
        verify(jwtUtil).isTokenExpired(VALID_TOKEN);
        verify(sessionDAO).findByToken(VALID_TOKEN);
        verify(jwtUtil).extractAllClaims(VALID_TOKEN);
    }

    @Test
    @DisplayName("getUserIdFromAuthHeader should return userId when auth header is valid")
    void getUserIdFromAuthHeader_WithValidHeader_ShouldReturnUserId() {
        // Given
        Session activeSession = new Session();
        activeSession.setActive(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(USER_ID);

        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(activeSession));
        when(jwtUtil.extractAllClaims(VALID_TOKEN)).thenReturn(claims);
        when(claims.get("userId")).thenReturn(USER_ID);
        when(userService.getUser(USER_ID)).thenReturn(userDTO);

        // When
        Long result = tokenService.getUserIdFromAuthHeader(VALID_AUTH_HEADER);

        // Then
        assertEquals(USER_ID, result);
        verify(jwtUtil).validateToken(VALID_TOKEN);
        verify(jwtUtil).isTokenExpired(VALID_TOKEN);
        verify(sessionDAO).findByToken(VALID_TOKEN);
        verify(jwtUtil).extractAllClaims(VALID_TOKEN);
    }

    @Test
    @DisplayName("getRoleFromAuthHeader should return role when auth header is valid")
    void getRoleFromAuthHeader_WithValidHeader_ShouldReturnRole() {
        // Given
        Session activeSession = new Session();
        activeSession.setActive(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(USER_ID);

        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(activeSession));
        when(jwtUtil.extractAllClaims(VALID_TOKEN)).thenReturn(claims);
        when(claims.get("userId")).thenReturn(USER_ID);
        when(claims.get("role", String.class)).thenReturn(ROLE_STRING);
        when(userService.getUser(USER_ID)).thenReturn(userDTO);

        // When
        Role result = tokenService.getRoleFromAuthHeader(VALID_AUTH_HEADER);

        // Then
        assertEquals(USER_ROLE, result);
        verify(jwtUtil).validateToken(VALID_TOKEN);
        verify(jwtUtil).isTokenExpired(VALID_TOKEN);
        verify(sessionDAO).findByToken(VALID_TOKEN);
        verify(jwtUtil).extractAllClaims(VALID_TOKEN);
    }

    @Test
    @DisplayName("isTokenActive should return true for active session")
    void isTokenActive_WithActiveSession_ShouldReturnTrue() {
        // Given
        Session activeSession = new Session();
        activeSession.setActive(true);
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(activeSession));

        // When
        boolean result = tokenService.isTokenActive(VALID_TOKEN);

        // Then
        assertTrue(result);
        verify(sessionDAO).findByToken(VALID_TOKEN);
    }

    @Test
    @DisplayName("isTokenActive should return false for inactive session")
    void isTokenActive_WithInactiveSession_ShouldReturnFalse() {
        // Given
        Session inactiveSession = new Session();
        inactiveSession.setActive(false);
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(inactiveSession));

        // When
        boolean result = tokenService.isTokenActive(VALID_TOKEN);

        // Then
        assertFalse(result);
        verify(sessionDAO).findByToken(VALID_TOKEN);
    }

    @Test
    @DisplayName("isTokenActive should return false when session not found")
    void isTokenActive_WithNonexistentSession_ShouldReturnFalse() {
        // Given
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.empty());

        // When
        boolean result = tokenService.isTokenActive(VALID_TOKEN);

        // Then
        assertFalse(result);
        verify(sessionDAO).findByToken(VALID_TOKEN);
    }

    @Test
    @DisplayName("getSessionByToken should return session when token exists")
    void getSessionByToken_WithValidToken_ShouldReturnSession() {
        // Given
        Session session = new Session();
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(session));

        // When
        Session result = tokenService.getSessionByToken(VALID_TOKEN);

        // Then
        assertNotNull(result);
        assertEquals(session, result);
        verify(sessionDAO).findByToken(VALID_TOKEN);
    }

    @Test
    @DisplayName("getSessionByToken should throw NotFoundException when token doesn't exist")
    void getSessionByToken_WithInvalidToken_ShouldThrowNotFoundException() {
        // Given
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class, () -> tokenService.getSessionByToken(VALID_TOKEN));
        verify(sessionDAO).findByToken(VALID_TOKEN);
    }

    @Test
    @DisplayName("Token validation should throw UnauthorizedException for invalid token")
    void tokenValidation_WithInvalidToken_ShouldThrowUnauthorizedException() {
        // Given
        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(false);

        // When/Then
        assertThrows(UnauthorizedException.class, () -> tokenService.getUserIdFromToken(VALID_TOKEN));
        verify(jwtUtil).validateToken(VALID_TOKEN);
        verifyNoMoreInteractions(jwtUtil);
    }

    @Test
    @DisplayName("Token validation should throw UnauthorizedException for expired token")
    void tokenValidation_WithExpiredToken_ShouldThrowUnauthorizedException() {
        // Given
        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.isTokenExpired(VALID_TOKEN)).thenReturn(true);

        // When/Then
        assertThrows(UnauthorizedException.class, () -> tokenService.getUserIdFromToken(VALID_TOKEN));
        verify(jwtUtil).validateToken(VALID_TOKEN);
        verify(jwtUtil).isTokenExpired(VALID_TOKEN);
        verifyNoMoreInteractions(jwtUtil);
    }

    @Test
    @DisplayName("Token validation should throw UnauthorizedException for inactive session")
    void tokenValidation_WithInactiveSession_ShouldThrowUnauthorizedException() {
        // Given
        Session inactiveSession = new Session();
        inactiveSession.setActive(false);
        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(inactiveSession));

        // When/Then
        assertThrows(UnauthorizedException.class, () -> tokenService.getUserIdFromToken(VALID_TOKEN));
        verify(jwtUtil).validateToken(VALID_TOKEN);
        verify(jwtUtil).isTokenExpired(VALID_TOKEN);
        verify(sessionDAO).findByToken(VALID_TOKEN);
    }

    @Test
    @DisplayName("Token validation should throw UnauthorizedException for invalid user")
    void tokenValidation_WithInvalidUser_ShouldThrowUnauthorizedException() {
        // Given
        Session activeSession = new Session();
        activeSession.setActive(true);
        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(activeSession));
        when(jwtUtil.extractAllClaims(VALID_TOKEN)).thenReturn(claims);
        when(claims.get("userId")).thenReturn(USER_ID);
        when(userService.getUser(USER_ID)).thenReturn(null);

        // When/Then
        assertThrows(UnauthorizedException.class, () -> tokenService.getUserIdFromToken(VALID_TOKEN));
        verify(jwtUtil).validateToken(VALID_TOKEN);
        verify(jwtUtil).isTokenExpired(VALID_TOKEN);
        verify(sessionDAO).findByToken(VALID_TOKEN);
        verify(jwtUtil).extractAllClaims(VALID_TOKEN);
        verify(userService).getUser(USER_ID);
    }

    @Test
    @DisplayName("getRoleFromAuthHeader should throw UnauthorizedException for invalid role")
    void getRoleFromAuthHeader_WithInvalidRole_ShouldThrowUnauthorizedException() {
        // Given
        Session activeSession = new Session();
        activeSession.setActive(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(USER_ID);

        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(sessionDAO.findByToken(VALID_TOKEN)).thenReturn(Optional.of(activeSession));
        when(jwtUtil.extractAllClaims(VALID_TOKEN)).thenReturn(claims);
        when(claims.get("userId")).thenReturn(USER_ID);
        when(claims.get("role", String.class)).thenReturn("INVALID_ROLE");
        when(userService.getUser(USER_ID)).thenReturn(userDTO);

        // When/Then
        assertThrows(UnauthorizedException.class, () -> tokenService.getRoleFromAuthHeader(VALID_AUTH_HEADER));
    }
}
