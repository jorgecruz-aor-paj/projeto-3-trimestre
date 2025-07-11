package aor.proj3t.unit.util.authutils;

import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.util.authutils.TokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the TokenUtil utility class.
 */
public class TokenUtilTest {

    private static final String VALID_TOKEN = "jwt-token-value";
    private static final String VALID_AUTH_HEADER = "Bearer " + VALID_TOKEN;
    private static final String INVALID_AUTH_HEADER = "Basic dXNlcjpwYXNzd29yZA==";
    private static final String MALFORMED_AUTH_HEADER = "Bearer";

    @Test
    @DisplayName("Should extract token from valid authorization header")
    public void testExtractToken() {
        // Act
        String token = TokenUtil.extractToken(VALID_AUTH_HEADER);
        
        // Assert
        assertEquals(VALID_TOKEN, token, "Extracted token should match the expected value");
    }
    
    @Test
    @DisplayName("Should throw UnauthorizedException for null authorization header")
    public void testExtractTokenNullHeader() {
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            TokenUtil.extractToken(null);
        }, "Should throw UnauthorizedException for null header");
    }
    
    @Test
    @DisplayName("Should throw UnauthorizedException for invalid authorization scheme")
    public void testExtractTokenInvalidScheme() {
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            TokenUtil.extractToken(INVALID_AUTH_HEADER);
        }, "Should throw UnauthorizedException for invalid scheme");
    }
    
    @Test
    @DisplayName("Should throw UnauthorizedException for malformed authorization header")
    public void testExtractTokenMalformedHeader() {
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            TokenUtil.extractToken(MALFORMED_AUTH_HEADER);
        }, "Should throw UnauthorizedException for malformed header");
    }
    
    @Test
    @DisplayName("Should validate authorization header successfully")
    public void testValidateAuthHeader() {
        // Act & Assert (no exception should be thrown)
        assertDoesNotThrow(() -> {
            TokenUtil.validateAuthHeader(VALID_AUTH_HEADER);
        }, "Should not throw exception for valid header");
    }
    
    @Test
    @DisplayName("Should throw UnauthorizedException when validating null header")
    public void testValidateAuthHeaderNull() {
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            TokenUtil.validateAuthHeader(null);
        }, "Should throw UnauthorizedException for null header");
    }
    
    @Test
    @DisplayName("Should throw UnauthorizedException when validating invalid scheme")
    public void testValidateAuthHeaderInvalidScheme() {
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            TokenUtil.validateAuthHeader(INVALID_AUTH_HEADER);
        }, "Should throw UnauthorizedException for invalid scheme");
    }
    
    @Test
    @DisplayName("Should check if header has token")
    public void testHasToken() {
        // Act & Assert
        assertTrue(TokenUtil.hasToken(VALID_AUTH_HEADER), "Valid header should have token");
        assertFalse(TokenUtil.hasToken(null), "Null header should not have token");
        assertFalse(TokenUtil.hasToken(INVALID_AUTH_HEADER), "Invalid scheme header should not have token");
        assertFalse(TokenUtil.hasToken(MALFORMED_AUTH_HEADER), "Malformed header should not have token");
    }
}
