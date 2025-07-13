package aor.proj3t.util.authutils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.exceptions.TwoFactorAuthException;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Utility class for Time-based One-Time Password (TOTP) operations.
 * 
 * This class provides methods for generating and validating TOTP codes
 * according to RFC 6238 specification. It handles secret key generation,
 * QR code data preparation, and TOTP code verification with time window
 * tolerance.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
@ApplicationScoped
public class TotpUtil {

    private static final Logger logger = LogManager.getLogger(TotpUtil.class);

    // TOTP configuration constants
    private static final int SECRET_KEY_LENGTH = 32; // 32 bytes = 256 bits
    private static final int CODE_DIGITS = 6;
    private static final int TIME_STEP = 30; // 30 seconds
    private static final int WINDOW_SIZE = 1; // Allow 1 step before/after current time
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    // Base32 alphabet
    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int[] BASE32_LOOKUP = new int[128];

    static {
        // Initialize Base32 lookup table
        for (int i = 0; i < BASE32_LOOKUP.length; i++) {
            BASE32_LOOKUP[i] = -1;
        }
        for (int i = 0; i < BASE32_ALPHABET.length(); i++) {
            BASE32_LOOKUP[BASE32_ALPHABET.charAt(i)] = i;
        }
    }

    /**
     * Generates a new random secret key for TOTP.
     * 
     * @return Base32-encoded secret key
     */
    public String generateSecretKey() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] secretBytes = new byte[SECRET_KEY_LENGTH];
            random.nextBytes(secretBytes);

            String secretKey = encodeBase32(secretBytes);
            logger.debug("Generated new TOTP secret key");
            return secretKey;
        } catch (Exception e) {
            logger.error("Failed to generate TOTP secret key", e);
            throw new TwoFactorAuthException("Failed to generate secret key", e.getMessage());
        }
    }

    /**
     * Generates backup codes for account recovery.
     * 
     * @param count the number of backup codes to generate
     * @return list of backup codes
     */
    public List<String> generateBackupCodes(int count) {
        List<String> backupCodes = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < count; i++) {
            // Generate 8-digit backup code
            String code = String.format("%08d", random.nextInt(100000000));
            backupCodes.add(code);
        }

        logger.debug("Generated {} backup codes", count);
        return backupCodes;
    }

    /**
     * Generates the Google Authenticator compatible QR code URL.
     * 
     * @param userEmail the user's email address
     * @param secretKey the Base32-encoded secret key
     * @param issuer    the name of the application/service
     * @return QR code URL for Google Authenticator
     */
    public String generateQrCodeUrl(String userEmail, String secretKey, String issuer) {
        try {
            String encodedIssuer = java.net.URLEncoder.encode(issuer, "UTF-8");
            String encodedEmail = java.net.URLEncoder.encode(userEmail, "UTF-8");

            return String.format(
                    "otpauth://totp/%s:%s?secret=%s&issuer=%s&digits=%d&period=%d",
                    encodedIssuer, encodedEmail, secretKey, encodedIssuer, CODE_DIGITS, TIME_STEP);
        } catch (java.io.UnsupportedEncodingException e) {
            logger.error("Failed to generate QR code URL", e);
            throw new TwoFactorAuthException("Failed to generate QR code", e.getMessage());
        }
    }

    /**
     * Verifies a TOTP code against the secret key.
     * 
     * @param code         the 6-digit TOTP code to verify
     * @param secretKey    the Base32-encoded secret key
     * @param lastUsedTime the timestamp when TOTP was last used (for replay
     *                     protection)
     * @return true if the code is valid and not a replay
     */
    public boolean verifyCode(String code, String secretKey, Instant lastUsedTime) {
        if (code == null || code.length() != CODE_DIGITS || secretKey == null) {
            logger.debug("Invalid input: code={}, codeLength={}, secretKey={}",
                    code, code != null ? code.length() : "null", secretKey != null ? "present" : "null");
            return false;
        }

        try {
            long currentTimeStep = getCurrentTimeStep();
            logger.debug("Verifying TOTP code: {} for time step: {}", code, currentTimeStep);

            // Check current time window and adjacent windows for clock skew tolerance
            for (int i = -WINDOW_SIZE; i <= WINDOW_SIZE; i++) {
                long timeStep = currentTimeStep + i;
                String expectedCode = generateCode(secretKey, timeStep);

                logger.debug("Time step {} (offset {}): expected code = {}, provided code = {}",
                        timeStep, i, expectedCode, code);

                if (code.equals(expectedCode)) {
                    // Check for replay attack
                    if (isReplayAttack(timeStep, lastUsedTime)) {
                        logger.warn("TOTP replay attack detected for time step: {}", timeStep);
                        return false;
                    }

                    logger.debug("TOTP code verified successfully for time step: {}", timeStep);
                    return true;
                }
            }

            logger.debug("TOTP code verification failed for all time windows");
            return false;
        } catch (Exception e) {
            logger.error("Error verifying TOTP code", e);
            return false;
        }
    }

    /**
     * Generates a TOTP code for the current time.
     * This method is primarily used for testing purposes.
     * 
     * @param secretKey the Base32-encoded secret key
     * @return the current TOTP code
     */
    public String generateCurrentCode(String secretKey) {
        return generateCode(secretKey, getCurrentTimeStep());
    }

    /**
     * Gets the current time step for TOTP calculation.
     * 
     * @return current time step
     */
    private long getCurrentTimeStep() {
        return Instant.now().getEpochSecond() / TIME_STEP;
    }

    /**
     * Generates a TOTP code for a specific time step.
     * 
     * @param secretKey the Base32-encoded secret key
     * @param timeStep  the time step to generate code for
     * @return the TOTP code
     */
    private String generateCode(String secretKey, long timeStep) {
        try {
            byte[] secretBytes = decodeBase32(secretKey);
            byte[] timeBytes = longToBytes(timeStep);

            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(secretBytes, HMAC_ALGORITHM);
            mac.init(keySpec);

            byte[] hash = mac.doFinal(timeBytes);

            // Dynamic truncation
            int offset = hash[hash.length - 1] & 0x0F;
            int truncatedHash = ((hash[offset] & 0x7F) << 24) |
                    ((hash[offset + 1] & 0xFF) << 16) |
                    ((hash[offset + 2] & 0xFF) << 8) |
                    (hash[offset + 3] & 0xFF);

            int code = truncatedHash % (int) Math.pow(10, CODE_DIGITS);

            return String.format("%0" + CODE_DIGITS + "d", code);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Error generating TOTP code", e);
            throw new TwoFactorAuthException("Failed to generate TOTP code", e.getMessage());
        }
    }

    /**
     * Converts a long value to byte array for HMAC calculation.
     * 
     * @param value the long value to convert
     * @return byte array representation
     */
    private byte[] longToBytes(long value) {
        byte[] bytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return bytes;
    }

    /**
     * Checks if the current verification attempt is a replay attack.
     * 
     * @param currentTimeStep the time step of the current code
     * @param lastUsedTime    the timestamp when TOTP was last successfully used
     * @return true if this is likely a replay attack
     */
    private boolean isReplayAttack(long currentTimeStep, Instant lastUsedTime) {
        if (lastUsedTime == null) {
            return false; // First time use
        }

        long lastUsedTimeStep = lastUsedTime.getEpochSecond() / TIME_STEP;

        // If the current time step is the same or older than the last used time step,
        // it's likely a replay attack
        return currentTimeStep <= lastUsedTimeStep;
    }

    /**
     * Validates that a secret key is properly formatted.
     * 
     * @param secretKey the secret key to validate
     * @return true if the secret key is valid
     */
    public boolean isValidSecretKey(String secretKey) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            return false;
        }

        try {
            byte[] decoded = decodeBase32(secretKey);
            return decoded.length >= 16; // Minimum 128 bits for security
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the current time step for external use (e.g., storing last used time).
     * 
     * @return current time step as Instant
     */
    public Instant getCurrentTimeStepAsInstant() {
        long timeStep = getCurrentTimeStep();
        return Instant.ofEpochSecond(timeStep * TIME_STEP);
    }

    /**
     * Encodes byte array to Base32 string.
     * 
     * @param data the byte array to encode
     * @return Base32 encoded string
     */
    private String encodeBase32(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int buffer = 0;
        int bitsLeft = 0;

        for (byte b : data) {
            buffer = (buffer << 8) | (b & 0xFF);
            bitsLeft += 8;

            while (bitsLeft >= 5) {
                result.append(BASE32_ALPHABET.charAt((buffer >> (bitsLeft - 5)) & 0x1F));
                bitsLeft -= 5;
            }
        }

        if (bitsLeft > 0) {
            result.append(BASE32_ALPHABET.charAt((buffer << (5 - bitsLeft)) & 0x1F));
        }

        return result.toString();
    }

    /**
     * Decodes Base32 string to byte array.
     * 
     * @param encoded the Base32 encoded string
     * @return decoded byte array
     * @throws IllegalArgumentException if the input is invalid
     */
    private byte[] decodeBase32(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return new byte[0];
        }

        encoded = encoded.toUpperCase().replaceAll("[^A-Z2-7]", "");

        if (encoded.isEmpty()) {
            return new byte[0];
        }

        List<Byte> result = new ArrayList<>();
        int buffer = 0;
        int bitsLeft = 0;

        for (char c : encoded.toCharArray()) {
            int value = BASE32_LOOKUP[c];
            if (value < 0) {
                throw new IllegalArgumentException("Invalid Base32 character: " + c);
            }

            buffer = (buffer << 5) | value;
            bitsLeft += 5;

            if (bitsLeft >= 8) {
                result.add((byte) ((buffer >> (bitsLeft - 8)) & 0xFF));
                bitsLeft -= 8;
            }
        }

        byte[] bytes = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            bytes[i] = result.get(i);
        }

        return bytes;
    }
}
