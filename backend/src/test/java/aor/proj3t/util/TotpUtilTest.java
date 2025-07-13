package aor.proj3t.util;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import aor.proj3t.util.authutils.TotpUtil;

/**
 * Test class for TotpUtil to debug TOTP code generation and verification.
 */
public class TotpUtilTest {

    private TotpUtil totpUtil;

    @BeforeEach
    void setUp() {
        totpUtil = new TotpUtil();
    }

    @Test
    void testSecretKeyGeneration() {
        String secretKey = totpUtil.generateSecretKey();
        assertNotNull(secretKey);
        assertFalse(secretKey.isEmpty());
        assertTrue(totpUtil.isValidSecretKey(secretKey));
        System.out.println("Generated secret key: " + secretKey);
    }

    @Test
    void testCodeGenerationAndVerification() {
        String secretKey = totpUtil.generateSecretKey();
        System.out.println("Secret key: " + secretKey);

        // Generate current code
        String currentCode = totpUtil.generateCurrentCode(secretKey);
        System.out.println("Generated code: " + currentCode);
        assertNotNull(currentCode);
        assertEquals(6, currentCode.length());

        // Verify the current code
        boolean isValid = totpUtil.verifyCode(currentCode, secretKey, null);
        System.out.println("Code verification result: " + isValid);
        assertTrue(isValid, "Current code should be valid");
    }

    @Test
    void testQrCodeUrlGeneration() {
        String secretKey = totpUtil.generateSecretKey();
        String email = "test@example.com";
        String issuer = "TestApp";

        String qrUrl = totpUtil.generateQrCodeUrl(email, secretKey, issuer);
        System.out.println("QR Code URL: " + qrUrl);

        assertNotNull(qrUrl);
        assertTrue(qrUrl.startsWith("otpauth://totp/"));
        assertTrue(qrUrl.contains(secretKey));
        assertTrue(qrUrl.contains("digits=6"));
        assertTrue(qrUrl.contains("period=30"));
    }

    @Test
    void testKnownSecretAndCode() {
        // Test with a known secret and expected codes
        String knownSecret = "JBSWY3DPEHPK3PXP"; // "Hello!" in Base32
        System.out.println("Known secret: " + knownSecret);

        // Generate code for current time
        String code = totpUtil.generateCurrentCode(knownSecret);
        System.out.println("Current code for known secret: " + code);

        // Verify it
        boolean isValid = totpUtil.verifyCode(code, knownSecret, null);
        System.out.println("Verification result: " + isValid);
        assertTrue(isValid);
    }

    @Test
    void testDebugCurrentTimeStep() {
        long currentTimeStep = Instant.now().getEpochSecond() / 30;
        Instant currentTime = Instant.now();
        System.out.println("Current time: " + currentTime);
        System.out.println("Current epoch seconds: " + currentTime.getEpochSecond());
        System.out.println("Current time step: " + currentTimeStep);
        System.out.println("Time step as instant: " + totpUtil.getCurrentTimeStepAsInstant());
    }

    @Test
    void testDetailedDebugWithMultipleCodes() {
        String secretKey = "JBSWY3DPEHPK3PXP"; // Known test secret

        Instant now = Instant.now();
        long currentTimeStep = now.getEpochSecond() / 30;

        System.out.println("=== DETAILED TOTP DEBUG ===");
        System.out.println("Secret Key: " + secretKey);
        System.out.println("Current Time: " + now);
        System.out.println("Current Epoch: " + now.getEpochSecond());
        System.out.println("Current Time Step: " + currentTimeStep);

        // Generate codes for current time and window
        for (int i = -2; i <= 2; i++) {
            long timeStep = currentTimeStep + i;
            String code = generateCodeAtTimeStep(secretKey, timeStep);
            Instant stepTime = Instant.ofEpochSecond(timeStep * 30);

            System.out.println("Time Step " + timeStep + " (" +
                    (i < 0 ? i : "+" + i) + "): " + code +
                    " at " + stepTime);
        }

        // Test verification with current code
        String currentCode = totpUtil.generateCurrentCode(secretKey);
        System.out.println("Current generated code: " + currentCode);

        boolean isValid = totpUtil.verifyCode(currentCode, secretKey, null);
        System.out.println("Verification result: " + isValid);

        assertTrue(isValid, "Current code should be valid");
    }

    @Test
    void testSetupVsLoginVerificationFlow() {
        String secretKey = totpUtil.generateSecretKey();
        System.out.println("=== SETUP VS LOGIN VERIFICATION FLOW ===");
        System.out.println("Secret Key: " + secretKey);

        // Simulate setup scenario - no lastUsedAt (should work)
        String setupCode = totpUtil.generateCurrentCode(secretKey);
        System.out.println("Setup Code: " + setupCode);

        boolean setupValid = totpUtil.verifyCode(setupCode, secretKey, null);
        System.out.println("Setup Verification (no lastUsedAt): " + setupValid);
        assertTrue(setupValid, "Setup verification should work");

        // Simulate setting lastUsedAt after setup
        Instant lastUsedAt = totpUtil.getCurrentTimeStepAsInstant();
        System.out.println("Last Used At: " + lastUsedAt);

        // Try to use the same code again - should fail due to replay protection
        boolean replayResult = totpUtil.verifyCode(setupCode, secretKey, lastUsedAt);
        System.out.println("Replay Verification (same code): " + replayResult);
        assertFalse(replayResult, "Replay should be detected and fail");

        // Wait a bit and generate a new code - should work
        try {
            Thread.sleep(1000); // Wait 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String newCode = totpUtil.generateCurrentCode(secretKey);
        System.out.println("New Code: " + newCode);

        // This might work or fail depending on whether we're in the same time step
        boolean newCodeResult = totpUtil.verifyCode(newCode, secretKey, lastUsedAt);
        System.out.println("New Code Verification: " + newCodeResult);

        // Test with a future time step (simulate next 30-second window)
        long futureTimeStep = (System.currentTimeMillis() / 1000 + 31) / 30;
        String futureCode = generateCodeAtTimeStep(secretKey, futureTimeStep);
        System.out.println("Future Code: " + futureCode);

        boolean futureResult = totpUtil.verifyCode(futureCode, secretKey, lastUsedAt);
        System.out.println("Future Code Verification: " + futureResult);
    }

    @Test
    void testRealWorldScenario() {
        System.out.println("=== REAL WORLD SCENARIO TEST ===");

        // Generate a secret key like the system does
        String secretKey = totpUtil.generateSecretKey();
        System.out.println("Generated Secret Key: " + secretKey);

        // Generate QR code URL
        String qrUrl = totpUtil.generateQrCodeUrl(secretKey, "test@example.com", "TestApp");
        System.out.println("QR Code URL: " + qrUrl);

        // Test current time
        Instant now = Instant.now();
        long currentTimeStep = now.getEpochSecond() / 30;
        System.out.println("Current Time: " + now);
        System.out.println("Current Time Step: " + currentTimeStep);

        // Generate the code that should be showing in Microsoft Authenticator
        String currentCode = totpUtil.generateCurrentCode(secretKey);
        System.out.println("Current TOTP Code: " + currentCode);

        // Test verification immediately (simulating user entering code quickly)
        boolean immediateVerification = totpUtil.verifyCode(currentCode, secretKey, null);
        System.out.println("Immediate Verification: " + immediateVerification);
        assertTrue(immediateVerification, "Immediate verification should succeed");

        // Test with some common issues

        // 1. Leading zeros stripped (some apps might do this)
        String codeWithoutLeadingZeros = currentCode.replaceFirst("^0+", "");
        if (!codeWithoutLeadingZeros.equals(currentCode) && codeWithoutLeadingZeros.length() < 6) {
            String paddedCode = String.format("%06d", Integer.parseInt(codeWithoutLeadingZeros));
            System.out.println("Testing code without leading zeros: " + codeWithoutLeadingZeros + " -> " + paddedCode);
            boolean paddedVerification = totpUtil.verifyCode(paddedCode, secretKey, null);
            System.out.println("Padded Verification: " + paddedVerification);
        }

        // 2. Test with exact Microsoft Authenticator behavior
        // Generate codes for a few time windows to see the pattern
        System.out.println("=== TIME WINDOW ANALYSIS ===");
        for (int i = -2; i <= 2; i++) {
            long timeStep = currentTimeStep + i;
            String code = generateCodeAtTimeStep(secretKey, timeStep);
            Instant stepTime = Instant.ofEpochSecond(timeStep * 30);
            System.out.println("Step " + timeStep + " (" + (i == 0 ? "current" : (i > 0 ? "+" + i : "" + i)) + "): " +
                    code + " at " + stepTime);
        }

        // 3. Test verification across time boundaries
        System.out.println("=== BOUNDARY TESTING ===");
        long secondsIntoCurrentStep = now.getEpochSecond() % 30;
        System.out.println("Seconds into current 30-sec window: " + secondsIntoCurrentStep);

        if (secondsIntoCurrentStep > 25) {
            System.out.println("Warning: Near end of time window, code might change soon");
        }

        // Test with previous and next codes
        String prevCode = generateCodeAtTimeStep(secretKey, currentTimeStep - 1);
        String nextCode = generateCodeAtTimeStep(secretKey, currentTimeStep + 1);

        System.out.println("Previous Code: " + prevCode);
        System.out.println("Next Code: " + nextCode);

        boolean prevVerification = totpUtil.verifyCode(prevCode, secretKey, null);
        boolean nextVerification = totpUtil.verifyCode(nextCode, secretKey, null);

        System.out.println("Previous Code Verification: " + prevVerification);
        System.out.println("Next Code Verification: " + nextVerification);

        // These should work due to WINDOW_SIZE = 1
        assertTrue(prevVerification || nextVerification, "At least one adjacent window should work");
    }

    @Test
    void testQrCodeParameterOrder() {
        System.out.println("=== QR CODE PARAMETER ORDER TEST ===");

        String secretKey = "JBSWY3DPEHPK3PXP"; // Known test secret
        String userEmail = "test@example.com";
        String issuer = "TestApp";

        // Generate QR code URL with correct parameter order
        String qrUrl = totpUtil.generateQrCodeUrl(userEmail, secretKey, issuer);
        System.out.println("QR Code URL: " + qrUrl);

        // Verify the QR code contains the correct secret
        assertTrue(qrUrl.contains("secret=" + secretKey), "QR code should contain the correct secret key");
        assertTrue(qrUrl.contains(":" + userEmail.replace("@", "%40")),
                "QR code should contain the user email in label");
        assertTrue(qrUrl.contains("issuer=" + issuer), "QR code should contain the issuer");

        // Generate a code using the secret from QR code and verify it works
        String currentCode = totpUtil.generateCurrentCode(secretKey);
        System.out.println("Generated code with secret: " + currentCode);

        boolean isValid = totpUtil.verifyCode(currentCode, secretKey, null);
        System.out.println("Verification result: " + isValid);
        assertTrue(isValid, "Code generated with correct secret should verify");

        // Test what would happen with wrong parameter order (the bug)
        String wrongQrUrl = totpUtil.generateQrCodeUrl(secretKey, userEmail, issuer);
        System.out.println("Wrong QR Code URL: " + wrongQrUrl);

        // This would have the email as the secret, which is wrong
        assertFalse(wrongQrUrl.contains("secret=" + secretKey),
                "Wrong parameter order should not contain correct secret");

        System.out.println("✅ QR Code parameter order is now correct!");
    }

    // Helper method to generate code at specific time step
    private String generateCodeAtTimeStep(String secretKey, long timeStep) {
        try {
            byte[] secretBytes = decodeBase32(secretKey);
            byte[] timeBytes = longToBytes(timeStep);

            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
            javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(secretBytes, "HmacSHA1");
            mac.init(keySpec);

            byte[] hash = mac.doFinal(timeBytes);

            int offset = hash[hash.length - 1] & 0x0F;
            int truncatedHash = ((hash[offset] & 0x7F) << 24) |
                    ((hash[offset + 1] & 0xFF) << 16) |
                    ((hash[offset + 2] & 0xFF) << 8) |
                    (hash[offset + 3] & 0xFF);

            int code = truncatedHash % 1000000;
            return String.format("%06d", code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] decodeBase32(String base32) {
        // Simple Base32 decoder for testing
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        int[] lookup = new int[128];
        for (int i = 0; i < alphabet.length(); i++) {
            lookup[alphabet.charAt(i)] = i;
        }

        base32 = base32.replaceAll("=", "").toUpperCase();
        int bits = 0;
        int value = 0;
        int index = 0;
        byte[] result = new byte[(base32.length() * 5) / 8];

        for (char c : base32.toCharArray()) {
            value = (value << 5) | lookup[c];
            bits += 5;

            if (bits >= 8) {
                result[index++] = (byte) ((value >> (bits - 8)) & 0xFF);
                bits -= 8;
            }
        }

        return java.util.Arrays.copyOf(result, index);
    }

    private byte[] longToBytes(long value) {
        byte[] bytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return bytes;
    }
}
