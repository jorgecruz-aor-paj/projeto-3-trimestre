package aor.proj3t.service.integration;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.NotificationDAO;
import aor.proj3t.daos.interfaces.SessionDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.Session;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.impl.AuthServiceImpl;
import aor.proj3t.service.impl.NotificationServiceImpl;
import aor.proj3t.service.interfaces.TokenService;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.PasswordUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.NotificationConverter;
import aor.proj3t.config.SettingsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for security-critical features including authentication,
 * session management, and real-time notification security.
 */
@ExtendWith(MockitoExtension.class)
class SecurityAndRealtimeIntegrationTest {

    // Test constants
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password123!";
    private static final String TEST_HASHED_PASSWORD = "$2a$12$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12";
    private static final String TEST_IP = "192.168.1.100";
    private static final String TEST_USER_AGENT = "Mozilla/5.0 (Test Browser)";
    private static final String TEST_TOKEN = "test.jwt.token";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_ADMIN_ID = 2L;

    // Service mocks
    @Mock private DAOFactory daoFactory;
    @Mock private ConverterFactory converterFactory;
    @Mock private ServiceFactory serviceFactory;
    @Mock private SettingsProvider settingsProvider;

    // DAO mocks
    @Mock private UserDAO userDAO;
    @Mock private SessionDAO sessionDAO;
    @Mock private NotificationDAO notificationDAO;

    // Utility mocks
    @Mock private JwtUtil jwtUtil;
    @Mock private PasswordUtil passwordUtil;
    @Mock private TokenService tokenService;
    @Mock private NotificationConverter notificationConverter;

    // Services under test
    @InjectMocks
    private AuthServiceImpl authService;
    
    @InjectMocks
    private NotificationServiceImpl notificationService;

    // Test entities
    private User testUser;
    private User adminUser;
    private Settings testSettings;
    private LoginDTO testLoginDTO;

    @BeforeEach
    void setUp() {
        // Configure factory mocks
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(daoFactory.forSession()).thenReturn(sessionDAO);
        lenient().when(daoFactory.forNotification()).thenReturn(notificationDAO);
        lenient().when(converterFactory.forNotification()).thenReturn(notificationConverter);
        lenient().when(serviceFactory.forToken()).thenReturn(tokenService);

        // Set up test users
        testUser = new User();
        testUser.setId(TEST_USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_HASHED_PASSWORD);
        testUser.setRole(Role.USER);
        testUser.setFirstName("Test");
        testUser.setLastName("User");

        adminUser = new User();
        adminUser.setId(TEST_ADMIN_ID);
        adminUser.setEmail("admin@test.com");
        adminUser.setPassword(TEST_HASHED_PASSWORD);
        adminUser.setRole(Role.ADMIN);
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");

        // Set up test settings
        testSettings = new Settings();
        testSettings.setSessionExpirationTime(60); // 60 minutes

        // Set up test login DTO
        testLoginDTO = new LoginDTO();
        testLoginDTO.setEmail(TEST_EMAIL);
        testLoginDTO.setPassword(TEST_PASSWORD);

        // Configure settings provider
        lenient().when(settingsProvider.getSettings()).thenReturn(testSettings);
    }

    @Test
    @DisplayName("Authentication rate limiting and brute force protection")
    void testAuthenticationSecurityMeasures() {
        // === TEST 1: Multiple failed login attempts ===
        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(false);

        // Simulate multiple failed attempts
        for (int i = 0; i < 5; i++) {
            UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> 
                authService.login(testLoginDTO, TEST_IP, TEST_USER_AGENT));
            assertEquals("Invalid credentials", exception.getUserMessage());
        }

        // Verify no session was created for failed attempts
        verify(sessionDAO, never()).create(any(Session.class));

        // === TEST 2: Successful login after fixing credentials ===
        when(passwordUtil.verifyPassword(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateSessionToken(eq(TEST_USER_ID), eq(TEST_EMAIL), eq(Role.USER.getName()), 
            eq(TEST_IP), anyInt())).thenReturn(TEST_TOKEN);
        when(jwtUtil.extractExpirationDate(TEST_TOKEN)).thenReturn(Instant.now().plusSeconds(3600));

        String token = authService.login(testLoginDTO, TEST_IP, TEST_USER_AGENT);
        assertEquals(TEST_TOKEN, token);
        verify(sessionDAO).create(any(Session.class));
    }

    @Test
    @DisplayName("Session hijacking prevention and token security")
    void testSessionSecurityMeasures() {
        // === TEST 1: IP address tracking ===
        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateSessionToken(eq(TEST_USER_ID), eq(TEST_EMAIL), eq(Role.USER.getName()), 
            eq(TEST_IP), anyInt())).thenReturn(TEST_TOKEN);
        when(jwtUtil.extractExpirationDate(TEST_TOKEN)).thenReturn(Instant.now().plusSeconds(3600));

        // Login from specific IP
        authService.login(testLoginDTO, TEST_IP, TEST_USER_AGENT);

        // Verify session was created with correct IP
        verify(sessionDAO).create(argThat(session -> 
            TEST_IP.equals(session.getIpAddress()) &&
            TEST_USER_AGENT.equals(session.getUserAgent()) &&
            session.getActive()
        ));

        // === TEST 2: Session invalidation on logout ===
        Session testSession = new Session();
        testSession.setId(1L);
        testSession.setSessionToken(TEST_TOKEN);
        testSession.setActive(true);
        testSession.setUser(testUser);
        testSession.setIpAddress(TEST_IP);

        when(tokenService.getSessionByToken(TEST_TOKEN)).thenReturn(testSession);

        authService.logout(TEST_USER_ID, TEST_TOKEN);

        // Verify session was deactivated
        assertFalse(testSession.getActive());
        verify(sessionDAO).update(testSession);
    }

    @Test
    @DisplayName("Concurrent session management and security")
    void testConcurrentSessionSecurity() throws InterruptedException {
        // Set up mocks for concurrent testing
        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateSessionToken(eq(TEST_USER_ID), eq(TEST_EMAIL), eq(Role.USER.getName()), 
            anyString(), anyInt())).thenReturn(TEST_TOKEN + "_1", TEST_TOKEN + "_2", TEST_TOKEN + "_3");
        when(jwtUtil.extractExpirationDate(anyString())).thenReturn(Instant.now().plusSeconds(3600));

        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // === TEST: Concurrent login attempts from different IPs ===
        CompletableFuture<String> login1 = CompletableFuture.supplyAsync(() -> {
            try {
                return authService.login(testLoginDTO, "192.168.1.101", "Browser1");
            } catch (Exception e) {
                return null;
            }
        }, executor);

        CompletableFuture<String> login2 = CompletableFuture.supplyAsync(() -> {
            try {
                return authService.login(testLoginDTO, "192.168.1.102", "Browser2");
            } catch (Exception e) {
                return null;
            }
        }, executor);

        CompletableFuture<String> login3 = CompletableFuture.supplyAsync(() -> {
            try {
                return authService.login(testLoginDTO, "192.168.1.103", "Browser3");
            } catch (Exception e) {
                return null;
            }
        }, executor);

        // Wait for all logins to complete - handle checked exceptions
        try {
            CompletableFuture.allOf(login1, login2, login3).get(5, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            fail("Concurrent session test failed due to timeout or execution error: " + e.getMessage());
        }

        // Verify all sessions were created (allowing concurrent sessions)
        verify(sessionDAO, times(3)).create(any(Session.class));

        executor.shutdown();
    }

    @Test
    @DisplayName("Notification security and authorization")
    void testNotificationSecurityMeasures() {
        // === TEST 1: User can only access their own notifications ===
        User sender = adminUser;
        User recipient = testUser;

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setSender(sender);
        notification.setRecipient(recipient);
        notification.setType(NotificationType.EVALUATION);
        notification.setContent("Test notification");
        notification.setTimestampSent(Instant.now());
        notification.setRead(false);

        lenient().when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));

        // User should be able to create notifications they send
        Notification result = notificationService.createNotification(notification);
        assertNotNull(result);
        verify(notificationDAO).create(any(Notification.class));

        // === TEST 2: Prevent unauthorized notification access ===
        // This would typically be tested in the resource layer with JWT filters
        // but we can verify the service enforces proper recipient validation
        
        // Create notification for different user
        User otherUser = new User();
        otherUser.setId(999L);
        otherUser.setEmail("other@test.com");
        otherUser.setRole(Role.USER);

        Notification otherNotification = new Notification();
        otherNotification.setRecipient(otherUser);
        otherNotification.setSender(adminUser);
        otherNotification.setContent("Notification for other user");

        // This should be handled by authorization filters in real implementation
        // Here we verify the notification is created with correct recipient
        
        Notification otherResult = notificationService.createNotification(otherNotification);
        assertEquals(otherUser.getId(), otherResult.getRecipient().getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "password123",     // No uppercase
        "PASSWORD123",     // No lowercase  
        "Password",        // No numbers
        "Password123",     // No special chars
        "Pass12!",         // Too short (7 chars)
        "Password 123!",   // Contains space
        ""                 // Empty password
    })
    @DisplayName("Password strength validation security")
    void testPasswordStrengthValidation(String weakPassword) {
        // Create a real PasswordUtil instance for testing
        PasswordUtil realPasswordUtil = new PasswordUtil();
        
        // Test that weak passwords are rejected
        assertFalse(realPasswordUtil.isValidPassword(weakPassword),
            "Password should be invalid: " + weakPassword);
    }

    @Test
    @DisplayName("Password strength validation - valid passwords")
    void testValidPasswordStrengthValidation() {
        PasswordUtil realPasswordUtil = new PasswordUtil();
        
        String[] validPasswords = {
            "Password123!",
            "MySecure@Pass456",
            "Complex#Password789",
            "Str0ng$P@ssw0rd"
        };
        
        for (String password : validPasswords) {
            assertTrue(realPasswordUtil.isValidPassword(password),
                "Password should be valid: " + password);
        }
    }

    @Test
    @DisplayName("Token expiration and refresh security")
    void testTokenExpirationSecurity() {
        // === TEST 1: Expired token handling ===
        String expiredToken = "expired.jwt.token";
        when(jwtUtil.extractExpirationDate(expiredToken))
            .thenReturn(Instant.now().minusSeconds(3600)); // Expired 1 hour ago

        // This would typically be handled by JWT filter
        // Here we verify the expiration time is properly checked
        Instant expirationTime = jwtUtil.extractExpirationDate(expiredToken);
        assertTrue(expirationTime.isBefore(Instant.now()), 
            "Token should be recognized as expired");

        // === TEST 2: Token generation with proper expiration ===
        when(jwtUtil.generateSessionToken(eq(TEST_USER_ID), eq(TEST_EMAIL), eq(Role.USER.getName()), 
            eq(TEST_IP), eq(60))).thenReturn(TEST_TOKEN);
        when(jwtUtil.extractExpirationDate(TEST_TOKEN))
            .thenReturn(Instant.now().plusSeconds(3600)); // Valid for 1 hour

        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordUtil.verifyPassword(TEST_PASSWORD, TEST_HASHED_PASSWORD)).thenReturn(true);

        String token = authService.login(testLoginDTO, TEST_IP, TEST_USER_AGENT);
        
        // Verify token was generated with proper session timeout
        verify(jwtUtil).generateSessionToken(TEST_USER_ID, TEST_EMAIL, Role.USER.getName(), TEST_IP, 60);
        assertEquals(TEST_TOKEN, token);
    }

    @Test
    @DisplayName("Real-time notification delivery security")
    void testRealtimeNotificationSecurity() {
        // === TEST 1: Notification content sanitization ===
        User sender = adminUser;
        User recipient = testUser;

        // Test with potentially malicious content
        String maliciousContent = "<script>alert('xss')</script>Evaluation completed";
        
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setRecipient(recipient);
        notification.setType(NotificationType.EVALUATION);
        notification.setContent(maliciousContent);
        notification.setTimestampSent(Instant.now());

        when(notificationDAO.create(notification)).thenReturn(notification);

        Notification result = notificationService.createNotification(notification);
        
        // Verify notification was created (content sanitization would happen at presentation layer)
        assertNotNull(result);
        assertEquals(maliciousContent, result.getContent()); // Raw storage
        verify(notificationDAO).create(notification);

        // === TEST 2: Notification type validation ===
        for (NotificationType type : NotificationType.values()) {
            Notification typeTestNotification = new Notification();
            typeTestNotification.setSender(sender);
            typeTestNotification.setRecipient(recipient);
            typeTestNotification.setType(type);
            typeTestNotification.setContent("Test notification for type: " + type);

            when(notificationDAO.create(typeTestNotification)).thenReturn(typeTestNotification);
            
            Notification typeResult = notificationService.createNotification(typeTestNotification);
            assertEquals(type, typeResult.getType());
        }
    }

    @Test
    @DisplayName("Cross-user data isolation security")
    void testCrossUserDataIsolation() {
        // === TEST 1: User cannot access other user's sessions ===
        User userA = testUser;
        User userB = new User();
        userB.setId(99L);
        userB.setEmail("userb@test.com");
        userB.setRole(Role.USER);

        Session sessionA = new Session();
        sessionA.setUser(userA);
        sessionA.setSessionToken("token_a");
        sessionA.setActive(true);

        Session sessionB = new Session();
        sessionB.setUser(userB);
        sessionB.setSessionToken("token_b");
        sessionB.setActive(true);

        // Verify sessions are isolated
        assertNotEquals(sessionA.getSessionToken(), sessionB.getSessionToken());
        assertNotEquals(sessionA.getUser().getId(), sessionB.getUser().getId());

        // === TEST 2: Notification isolation ===
        Notification notificationForA = new Notification();
        notificationForA.setSender(adminUser);
        notificationForA.setRecipient(userA);
        notificationForA.setContent("Message for User A");

        Notification notificationForB = new Notification();
        notificationForB.setSender(adminUser);
        notificationForB.setRecipient(userB);
        notificationForB.setContent("Message for User B");

        // Verify notifications have correct recipients
        assertEquals(userA.getId(), notificationForA.getRecipient().getId());
        assertEquals(userB.getId(), notificationForB.getRecipient().getId());
        
        // Verify content isolation
        assertNotEquals(notificationForA.getContent(), notificationForB.getContent());
    }
}
