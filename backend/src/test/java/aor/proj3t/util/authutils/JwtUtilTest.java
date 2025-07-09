package aor.proj3t.util.authutils;

import aor.proj3t.enums.TokenType;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private Dotenv dotenv;

    private final String TEST_SECRET = "dGVzdFNlY3JldEtleUZvclRlc3RpbmdKd3RVdGlsQ2xhc3NUaGF0TmVlZHNUb0JlQXRMZWFzdDMyQnl0ZXNMb25n";
    private final String TEST_ISSUER = "test-issuer";
    private final String TEST_EMAIL = "test@example.com";
    private final Long TEST_USER_ID = 1L;
    private final String TEST_ROLE = "USER";
    private final String TEST_IP = "127.0.0.1";

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // Mock the dotenv to return our test values
        lenient().when(dotenv.get("JWT_SECRET")).thenReturn(TEST_SECRET);
        lenient().when(dotenv.get("JWT_ISSUER")).thenReturn(TEST_ISSUER);
        
        // Set the fields directly since we can't easily mock @PostConstruct
        java.lang.reflect.Field secretKeyField = JwtUtil.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtUtil, TEST_SECRET);
        
        java.lang.reflect.Field issuerField = JwtUtil.class.getDeclaredField("issuer");
        issuerField.setAccessible(true);
        issuerField.set(jwtUtil, TEST_ISSUER);
    }

    @Test
    @DisplayName("Should generate a valid session token")
    public void testGenerateSessionToken() {
        // Arrange
        Integer validityMinutes = 60;

        // Act
        String token = jwtUtil.generateSessionToken(TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_IP, validityMinutes);

        // Assert
        assertNotNull(token, "Token should not be null");
        assertTrue(jwtUtil.validateToken(token), "Token should be valid");
        
        // Verify token claims
        Claims claims = jwtUtil.extractAllClaims(token);
        assertEquals(TEST_EMAIL, claims.getSubject(), "Subject should match the email");
        assertEquals(TEST_USER_ID, claims.get("userId", Long.class), "userId claim should match test userId");
        assertEquals(TEST_EMAIL, claims.get("email"), "email claim should match test email");
        assertEquals(TEST_ROLE, claims.get("role"), "role claim should match test role");
        assertEquals(TEST_IP, claims.get("ipAddress"), "ipAddress claim should match test IP");
        assertEquals(TokenType.SESSION.name(), claims.get("tokenType"), "tokenType claim should be SESSION");
        
        // Verify expiration time is approximately validityMinutes in the future
        Instant expiration = jwtUtil.extractExpirationDate(token);
        Instant now = Instant.now();
        long minutesDifference = ChronoUnit.MINUTES.between(now, expiration);
        assertTrue(minutesDifference <= validityMinutes && minutesDifference > validityMinutes - 2, 
                   "Token expiration should be within 2 minutes of the expected validity period");
    }

    @Test
    @DisplayName("Should validate token correctly")
    public void testValidateToken() {
        // Arrange
        String token = jwtUtil.generateSessionToken(TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_IP, 60);
        
        // Act & Assert
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Should reject invalid token")
    public void testInvalidToken() {
        // Arrange
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        
        // Act & Assert
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    @DisplayName("Should extract user ID correctly")
    public void testExtractUserId() {
        // Arrange
        String token = jwtUtil.generateSessionToken(TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_IP, 60);
        
        // Act
        Long userId = jwtUtil.extractUserId(token);
        
        // Assert
        assertEquals(TEST_USER_ID, userId);
    }

    @Test
    @DisplayName("Should extract user email correctly")
    public void testExtractUserEmail() {
        // Arrange
        String token = jwtUtil.generateSessionToken(TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_IP, 60);
        
        // Act
        String email = jwtUtil.extractUserEmail(token);
        
        // Assert
        assertEquals(TEST_EMAIL, email);
    }

    @Test
    @DisplayName("Should extract user role correctly")
    public void testExtractUserRole() {
        // Arrange
        String token = jwtUtil.generateSessionToken(TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_IP, 60);
        
        // Act
        String role = jwtUtil.extractUserRole(token);
        
        // Assert
        assertEquals(TEST_ROLE, role);
    }

    @Test
    @DisplayName("Should detect expired token")
    public void testIsTokenExpired() throws InterruptedException {
        // Arrange - create a token with very short expiration
        String token = jwtUtil.generateSessionToken(TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_IP, 0);
        
        // Give it a moment to ensure it expires
        Thread.sleep(1500);
        
        // Act & Assert
        try {
            // This should throw an ExpiredJwtException when trying to extract claims
            jwtUtil.extractAllClaims(token);
            fail("Expected ExpiredJwtException was not thrown");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // This is the expected behavior for an expired token
            assertTrue(true, "Token correctly identified as expired");
        }
        
        // Alternatively, validate that validateToken returns false for expired tokens
        assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Should generate confirmation token correctly")
    public void testGenerateConfirmationToken() {
        // Arrange
        Integer validityMinutes = 60;

        // Act
        String token = jwtUtil.generateConfirmationToken(TEST_EMAIL, TEST_IP, validityMinutes);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        
        // Verify token claims
        Claims claims = jwtUtil.extractAllClaims(token);
        assertEquals(TEST_EMAIL, claims.getSubject());
        assertEquals(TEST_EMAIL, claims.get("email"));
        assertEquals(TEST_IP, claims.get("ipAddress"));
        assertEquals(TokenType.ACCOUNT_CONFIRMATION.name(), claims.get("tokenType"));
    }

    @Test
    @DisplayName("Should generate password reset token correctly")
    public void testGeneratePasswordResetToken() {
        // Arrange
        Integer validityMinutes = 60;

        // Act
        String token = jwtUtil.generatePasswordResetToken(TEST_EMAIL, TEST_USER_ID, TEST_IP, validityMinutes);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        
        // Verify token claims
        Claims claims = jwtUtil.extractAllClaims(token);
        assertEquals(TEST_EMAIL, claims.getSubject());
        assertEquals(TEST_EMAIL, claims.get("email"));
        assertEquals(TEST_IP, claims.get("ipAddress"));
        
        // Get userId as Long to match the expected type
        Long userId = Long.valueOf(claims.get("userId").toString());
        assertEquals(TEST_USER_ID, userId);
        
        assertEquals(TokenType.PASSWORD_RESET.name(), claims.get("tokenType"));
    }
}
