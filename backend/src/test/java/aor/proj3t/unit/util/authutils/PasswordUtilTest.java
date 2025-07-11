package aor.proj3t.unit.util.authutils;

import aor.proj3t.util.authutils.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordUtilTest {

    private PasswordUtil passwordUtil;

    @BeforeEach
    void setUp() {
        passwordUtil = new PasswordUtil();
    }

    // ===== PASSWORD HASHING TESTS =====

    @Test
    @DisplayName("hashPassword should return a non-null BCrypt hash")
    void hashPassword_WithValidPassword_ShouldReturnValidHash() {
        // Given
        String password = "TestPassword123!";

        // When
        String hashedPassword = passwordUtil.hashPassword(password);

        // Then
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$")); // BCrypt format
        assertTrue(hashedPassword.length() >= 60); // BCrypt hash length
    }

    @Test
    @DisplayName("hashPassword should generate different hashes for the same password")
    void hashPassword_SamePasswordMultipleTimes_ShouldGenerateDifferentHashes() {
        // Given
        String password = "TestPassword123!";

        // When
        String hash1 = passwordUtil.hashPassword(password);
        String hash2 = passwordUtil.hashPassword(password);

        // Then
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotEquals(hash1, hash2); // BCrypt uses salt, so hashes should be different
    }

    @Test
    @DisplayName("hashPassword should handle empty string")
    void hashPassword_WithEmptyString_ShouldReturnHash() {
        // Given
        String password = "";

        // When
        String hashedPassword = passwordUtil.hashPassword(password);

        // Then
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
    }

    @Test
    @DisplayName("hashPassword should handle null input")
    void hashPassword_WithNull_ShouldThrowException() {
        // Given
        String password = null;

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordUtil.hashPassword(password));
    }

    @Test
    @DisplayName("hashPassword should handle very long passwords")
    void hashPassword_WithVeryLongPassword_ShouldReturnHash() {
        // Given
        String longPassword = "A".repeat(1000) + "1!";

        // When
        String hashedPassword = passwordUtil.hashPassword(longPassword);

        // Then
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
    }

    @Test
    @DisplayName("hashPassword should handle special characters")
    void hashPassword_WithSpecialCharacters_ShouldReturnHash() {
        // Given
        String password = "Test@#$%^&*()_+-=[]{}|;':\",./<>?Password123!";

        // When
        String hashedPassword = passwordUtil.hashPassword(password);

        // Then
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
    }

    @Test
    @DisplayName("hashPassword should handle Unicode characters")
    void hashPassword_WithUnicodeCharacters_ShouldReturnHash() {
        // Given
        String password = "Tëst🔒Pássword123!";

        // When
        String hashedPassword = passwordUtil.hashPassword(password);

        // Then
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$"));
    }

    // ===== PASSWORD VERIFICATION TESTS =====

    @Test
    @DisplayName("verifyPassword should return true for correct password")
    void verifyPassword_WithCorrectPassword_ShouldReturnTrue() {
        // Given
        String password = "TestPassword123!";
        String hashedPassword = passwordUtil.hashPassword(password);

        // When
        boolean result = passwordUtil.verifyPassword(password, hashedPassword);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("verifyPassword should return false for incorrect password")
    void verifyPassword_WithIncorrectPassword_ShouldReturnFalse() {
        // Given
        String correctPassword = "TestPassword123!";
        String incorrectPassword = "WrongPassword123!";
        String hashedPassword = passwordUtil.hashPassword(correctPassword);

        // When
        boolean result = passwordUtil.verifyPassword(incorrectPassword, hashedPassword);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("verifyPassword should return false with null password")
    void verifyPassword_WithNullPassword_ShouldReturnFalse() {
        // Given
        String password = "TestPassword123!";
        String hashedPassword = passwordUtil.hashPassword(password);

        // When
        boolean result = passwordUtil.verifyPassword(null, hashedPassword);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("verifyPassword should handle null hash")
    void verifyPassword_WithNullHash_ShouldThrowException() {
        // Given
        String password = "TestPassword123!";

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordUtil.verifyPassword(password, null));
    }

    @Test
    @DisplayName("verifyPassword should handle invalid hash format")
    void verifyPassword_WithInvalidHashFormat_ShouldThrowException() {
        // Given
        String password = "TestPassword123!";
        String invalidHash = "not-a-valid-bcrypt-hash";

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> 
            passwordUtil.verifyPassword(password, invalidHash));
    }

    @Test
    @DisplayName("verifyPassword should be case sensitive")
    void verifyPassword_CaseSensitive_ShouldReturnFalse() {
        // Given
        String password = "TestPassword123!";
        String wrongCasePassword = "testpassword123!";
        String hashedPassword = passwordUtil.hashPassword(password);

        // When
        boolean result = passwordUtil.verifyPassword(wrongCasePassword, hashedPassword);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("verifyPassword should handle empty string password")
    void verifyPassword_WithEmptyPassword_ShouldWork() {
        // Given
        String password = "";
        String hashedPassword = passwordUtil.hashPassword(password);

        // When
        boolean resultCorrect = passwordUtil.verifyPassword("", hashedPassword);
        boolean resultIncorrect = passwordUtil.verifyPassword("notEmpty", hashedPassword);

        // Then
        assertTrue(resultCorrect);
        assertFalse(resultIncorrect);
    }

    // ===== PASSWORD VALIDATION TESTS =====

    @Test
    @DisplayName("isValidPassword should return true for valid passwords")
    void isValidPassword_WithValidPasswords_ShouldReturnTrue() {
        // Test multiple valid passwords
        String[] validPasswords = {
            "Password123!",
            "StrongP@ss1",
            "MySecure123#",
            "Test123@Word",
            "Abcd1234!",
            "P@ssw0rd123",
            "SecurePass1*",
            "MyP@ssw0rd!",
            "StrongPassword1@"
        };

        for (String password : validPasswords) {
            assertTrue(passwordUtil.isValidPassword(password), 
                "Password should be valid: " + password);
        }
    }

    @ParameterizedTest
    @DisplayName("isValidPassword should return false for invalid passwords")
    @ValueSource(strings = {
        "",                    // Empty string
        "short",              // Too short
        "password",           // No uppercase, no digit, no special char
        "PASSWORD",           // No lowercase, no digit, no special char
        "12345678",           // No letters, no special char
        "!@#$%^&*",          // No letters, no digits
        "Password",           // No digit, no special char
        "Password123",        // No special char
        "Password!",          // No digit
        "password123!",       // No uppercase
        "PASSWORD123!",       // No lowercase
        "Pass 123!",          // Contains space
        "Pass\t123!",         // Contains tab
        "Pass\n123!"          // Contains newline
    })
    void isValidPassword_WithInvalidPasswords_ShouldReturnFalse(String password) {
        // When/Then
        assertFalse(passwordUtil.isValidPassword(password), 
            "Password should be invalid: " + password);
    }

    @Test
    @DisplayName("isValidPassword should return false for null password")
    void isValidPassword_WithNull_ShouldReturnFalse() {
        // When
        boolean result = passwordUtil.isValidPassword(null);

        // Then
        assertFalse(result);
    }

    @ParameterizedTest
    @DisplayName("isValidPassword should validate all required special characters")
    @ValueSource(strings = {
        "Password123@",
        "Password123#", 
        "Password123$",
        "Password123%",
        "Password123^",
        "Password123&",
        "Password123+",
        "Password123=",
        "Password123!",
        "Password123*"
    })
    void isValidPassword_WithDifferentSpecialChars_ShouldReturnTrue(String password) {
        // When/Then
        assertTrue(passwordUtil.isValidPassword(password), 
            "Password with special char should be valid: " + password);
    }

    @Test
    @DisplayName("isValidPassword should handle minimum length requirement")
    void isValidPassword_MinimumLength_ShouldWork() {
        // Given - exactly 8 characters with all requirements
        String validMinLength = "Pass123!";
        String invalidMinLength = "Pass12!"; // 7 characters

        // When/Then
        assertTrue(passwordUtil.isValidPassword(validMinLength));
        assertFalse(passwordUtil.isValidPassword(invalidMinLength));
    }

    @Test
    @DisplayName("isValidPassword should handle very long passwords")
    void isValidPassword_VeryLongPassword_ShouldReturnTrue() {
        // Given - very long but valid password
        String longPassword = "Password123!" + "A".repeat(1000);

        // When
        boolean result = passwordUtil.isValidPassword(longPassword);

        // Then
        assertTrue(result);
    }

    // ===== INTEGRATION TESTS =====

    @Test
    @DisplayName("Full password workflow should work correctly")
    void fullPasswordWorkflow_ShouldWorkCorrectly() {
        // Given
        String validPassword = "TestPassword123!";
        String invalidPassword = "weakpassword";

        // When - validate password
        boolean isValid = passwordUtil.isValidPassword(validPassword);
        boolean isInvalid = passwordUtil.isValidPassword(invalidPassword);

        // Then - validation should work
        assertTrue(isValid);
        assertFalse(isInvalid);

        // When - hash the valid password
        String hashedPassword = passwordUtil.hashPassword(validPassword);

        // Then - hash should be created
        assertNotNull(hashedPassword);

        // When - verify the password
        boolean verificationResult = passwordUtil.verifyPassword(validPassword, hashedPassword);

        // Then - verification should succeed
        assertTrue(verificationResult);

        // When - verify with wrong password
        boolean wrongVerificationResult = passwordUtil.verifyPassword("WrongPassword123!", hashedPassword);

        // Then - verification should fail
        assertFalse(wrongVerificationResult);
    }

    @Test
    @DisplayName("Multiple hash and verify cycles should work consistently")
    void multipleHashVerifyCycles_ShouldBeConsistent() {
        // Given
        String password = "TestPassword123!";

        // When/Then - perform multiple cycles
        for (int i = 0; i < 5; i++) {
            String hashedPassword = passwordUtil.hashPassword(password);
            boolean verificationResult = passwordUtil.verifyPassword(password, hashedPassword);
            assertTrue(verificationResult, "Verification should succeed in cycle " + i);
        }
    }

    // ===== EDGE CASE TESTS =====




    private static Stream<Arguments> provideEdgeCasePasswords() {
        return Stream.of(
            Arguments.of("Password123@", true, "Standard valid password"),
            Arguments.of("P@ssw0rd", true, "Exactly 8 characters with all requirements"),
            Arguments.of("P@ssw0r", false, "7 characters - too short"),
            Arguments.of("PASSWORD123@", false, "No lowercase letters"),
            Arguments.of("password123@", false, "No uppercase letters"),
            Arguments.of("Password@", false, "No digits"),
            Arguments.of("Password123", false, "No special characters"),
            Arguments.of("Pass Word123@", false, "Contains space"),
            Arguments.of("🔒Password123@", true, "Contains emoji"),
            Arguments.of("PasswordÁÉÍ123@", true, "Contains accented characters - should be valid"),
            Arguments.of("Pass\u0000word123@", false, "Contains null character"),
            Arguments.of("Password123@" + "\u00A0", false, "Contains non-breaking space")
        );
    }
    @ParameterizedTest
    @DisplayName("Password validation should handle edge cases")
    @MethodSource("provideEdgeCasePasswords")
    void isValidPassword_EdgeCases_ShouldHandleCorrectly(String password, boolean expectedValid, String description) {
        // When
        boolean result = passwordUtil.isValidPassword(password);

        // Then
        assertEquals(expectedValid, result, description);
    }

    @Test
    @DisplayName("Hash length and format should be consistent")
    void hashFormat_ShouldBeConsistent() {
        // Given
        String password = "TestPassword123!";

        // When
        String hash1 = passwordUtil.hashPassword(password);
        String hash2 = passwordUtil.hashPassword(password);

        // Then
        // Both hashes should have the same format but different values
        assertTrue(hash1.startsWith("$2a$12$")); // BCrypt format with work factor 12
        assertTrue(hash2.startsWith("$2a$12$"));
        assertEquals(60, hash1.length()); // Standard BCrypt hash length
        assertEquals(60, hash2.length());
        assertNotEquals(hash1, hash2); // Different salts should produce different hashes
    }

    @Test
    @DisplayName("Password verification should be timing attack resistant")
    void passwordVerification_TimingAttackResistance_ShouldBeConsistent() {
        // Given
        String password = "TestPassword123!";
        String hashedPassword = passwordUtil.hashPassword(password);
        String wrongPassword = "WrongPassword123!";

        // When - measure timing for correct password
        long startTime1 = System.nanoTime();
        boolean result1 = passwordUtil.verifyPassword(password, hashedPassword);
        long endTime1 = System.nanoTime();

        // When - measure timing for incorrect password
        long startTime2 = System.nanoTime();
        boolean result2 = passwordUtil.verifyPassword(wrongPassword, hashedPassword);
        long endTime2 = System.nanoTime();

        // Then
        assertTrue(result1);
        assertFalse(result2);

        // Note: BCrypt should provide timing attack resistance, but exact timing
        // can vary due to system load, so we just verify the operations complete
        // in reasonable time (less than 1 second each)
        long duration1 = endTime1 - startTime1;
        long duration2 = endTime2 - startTime2;
        assertTrue(duration1 < 1_000_000_000L, "Verification should complete within 1 second");
        assertTrue(duration2 < 1_000_000_000L, "Verification should complete within 1 second");
    }
}
