package aor.proj3t.service.impl;

import aor.proj3t.config.SettingsProvider;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.SessionDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.TokenService;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private UserDAO userDAO;

    @Mock
    private SessionDAO sessionDAO;

    @Mock
    private SettingsProvider settingsProvider;

    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private TokenService tokenService;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "Password123!";
    private final String TEST_HASHED_PASSWORD = "$2a$10$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12";
    private final String TEST_IP = "127.0.0.1";
    private final String TEST_USER_AGENT = "Mozilla/5.0";
    private final String TEST_TOKEN = "test.jwt.token";
    private final Long TEST_USER_ID = 1L;

    private User testUser;
    private LoginDTO testLoginDTO;
    private Settings testSettings;

    @BeforeEach
    public void setup() {
        // Create test user using the Role enum
        testUser = new User();
        testUser.setId(TEST_USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_HASHED_PASSWORD);
        testUser.setRole(Role.USER);

        // Create test login DTO
        testLoginDTO = new LoginDTO();
        testLoginDTO.setEmail(TEST_EMAIL);
        testLoginDTO.setPassword(TEST_PASSWORD);

        // Create test settings
        testSettings = new Settings();
        testSettings.setSessionExpirationTime(60);

        // Configure mocks with lenient stubbing to avoid UnnecessaryStubbingException
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(daoFactory.forSession()).thenReturn(sessionDAO);
        lenient().when(settingsProvider.getSettings()).thenReturn(testSettings);
    }

    @Test
    @DisplayName("Login should succeed with valid credentials")
    public void testLoginSuccess() {
        // Arrange
        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateSessionToken(eq(TEST_USER_ID), eq(TEST_EMAIL), eq(Role.USER.getName()), eq(TEST_IP), anyInt()))
                .thenReturn(TEST_TOKEN);
        when(jwtUtil.extractExpirationDate(TEST_TOKEN)).thenReturn(Instant.now().plusSeconds(3600));

        // Act
        String token = authService.login(testLoginDTO, TEST_IP, TEST_USER_AGENT);

        // Assert
        assertEquals(TEST_TOKEN, token);
        
        // Verify session creation
        verify(sessionDAO).create(any(Session.class));
    }

    @Test
    @DisplayName("Login should fail with non-existent user")
    public void testLoginFailUserNotFound() {
        // Arrange
        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            authService.login(testLoginDTO, TEST_IP, TEST_USER_AGENT);
        });
        
        // Check the user message, not the detailed dev message
        UnauthorizedException unauthorizedException = (UnauthorizedException) exception;
        assertEquals("Invalid credentials", unauthorizedException.getUserMessage());
        
        // Verify no session creation occurred
        verify(sessionDAO, never()).create(any(Session.class));
    }

    @Test
    @DisplayName("Login should fail with incorrect password")
    public void testLoginFailInvalidPassword() {
        // Arrange
        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            authService.login(testLoginDTO, TEST_IP, TEST_USER_AGENT);
        });
        
        // Check the user message, not the detailed dev message
        UnauthorizedException unauthorizedException = (UnauthorizedException) exception;
        assertEquals("Invalid credentials", unauthorizedException.getUserMessage());
        
        // Verify no session creation occurred
        verify(sessionDAO, never()).create(any(Session.class));
    }

    @Test
    @DisplayName("Logout should mark session as inactive")
    public void testLogout() {
        // Arrange
        Session session = new Session();
        session.setId(1L);
        session.setSessionToken(TEST_TOKEN);
        session.setActive(true);

        lenient().when(serviceFactory.forToken()).thenReturn(tokenService);
        lenient().when(tokenService.getSessionByToken(TEST_TOKEN)).thenReturn(session);

        // Act
        authService.logout(TEST_USER_ID, TEST_TOKEN);

        // Assert
        assertFalse(session.getActive());
        verify(sessionDAO).update(session);
    }
}
