package aor.proj3t.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.enums.TokenType;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Utility class for handling JWT (JSON Web Tokens) operations.
 */
@ApplicationScoped
public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    private static final int DEFAULT_TOKEN_EXPIRATION_TIME = 60; // value in minutes
    private String secretKey;
    private String issuer;

    @PostConstruct
    public void init() {
        logger.info("Initializing JWT utility service");
        try {
            // Dotenv configuration
            logger.debug("Loading environment variables for JWT configuration");
            Dotenv dotenv = Dotenv
                    .configure()
                    .ignoreIfMalformed()
                    .load();
            // Load JWT configuration from environment variables
            secretKey = dotenv.get("JWT_SECRET");
            issuer = dotenv.get("JWT_ISSUER");

            if (secretKey == null || secretKey.isEmpty()) {
                logger.warn("JWT_SECRET environment variable is not set or empty");
            }
            if (issuer == null || issuer.isEmpty()) {
                logger.warn("JWT_ISSUER environment variable is not set or empty");
            }

            logger.info("JWT utility service initialized successfully");
        } catch (DotenvException e) {
            logger.error("Failed to load JWT configuration from environment variables", e);
        } catch (Exception e) {
            logger.error("Unexpected error during JWT utility initialization", e);
        }
    }

    /**
     * Generate a JWT token for a user.
     * 
     * @param userId          The user ID
     * @param userEmail       The user email
     * @param userRole        The user role
     * @param ipAddress       The IP address of the client
     * @param validityMinutes The validity period in minutes
     * @return The JWT token
     */
    public String generateSessionToken(Long userId, String userEmail, String userRole, String ipAddress,
            Integer validityMinutes) {
        logger.debug("Generating JWT token for user [ID={}] with email [{}] and role [{}]", userId, userEmail,
                userRole);

        int minutes = (validityMinutes != null) ? validityMinutes : DEFAULT_TOKEN_EXPIRATION_TIME;
        logger.debug("Token will be valid for {} minutes", minutes);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", userEmail);
        claims.put("role", userRole);
        claims.put("ipAddress", ipAddress);

        try {
            String token = createToken(userEmail, TokenType.SESSION, claims, minutes);
            logger.info("JWT token generated successfully for user [ID={}]", userId);
            return token;
        } catch (Exception e) {
            logger.error("Failed to generate JWT token for user [ID={}]", userId, e);
            throw e;
        }
    }

    /**
     * Generate a confirmation token for account validation.
     * 
     * @param userEmail       The user email to be confirmed
     * @param ipAddress       The IP address of the client
     * @param validityMinutes The validity period in minutes, or null to use the
     *                        default
     * @return The JWT confirmation token
     */
    public String generateConfirmationToken(String userEmail, String ipAddress, Integer validityMinutes) {
        int minutes = (validityMinutes != null) ? validityMinutes : DEFAULT_TOKEN_EXPIRATION_TIME;

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userEmail);
        claims.put("ipAddress", ipAddress);

        try {
            String token = createToken(userEmail, TokenType.ACCOUNT_CONFIRMATION, claims, minutes);

            logger.info("JWT token generated successfully for email [EMAIL={}]", userEmail);
            return token;
        } catch (Exception e) {
            logger.error("Failed to generate JWT token for email [EMAIL={}]", userEmail, e);
            throw e;
        }
    }

    /**
     * Generate a password reset token for a user.
     * 
     * @param userEmail       The user email for which the password reset is
     *                        requested
     * @param ipAddress       The IP address of the client
     * @param validityMinutes The validity period in minutes, or null to use the
     *                        default
     * @return The JWT password reset token
     */
    public String generatePasswordResetToken(String userEmail, String ipAddress, Integer validityMinutes) {
        int minutes = (validityMinutes != null) ? validityMinutes : DEFAULT_TOKEN_EXPIRATION_TIME;

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userEmail);
        claims.put("ipAddress", ipAddress);

        try {
            String token = createToken(userEmail, TokenType.PASSWORD_RESET, claims, minutes);

            logger.info("JWT token generated successfully for email [EMAIL={}]", userEmail);
            return token;
        } catch (Exception e) {
            logger.error("Failed to generate JWT token for email [EMAIL={}]", userEmail, e);
            throw e;
        }
    }

    /**
     * Create a JWT token with the specified subject, token type, additional claims
     * and validity period.
     * 
     * @param subject          The subject of the token (typically the
     *                         username/email)
     * @param tokenType        The type of token being created (e.g., SESSION,
     *                         ACCOUNT_CONFIRMATION)
     * @param additionalClaims Additional claims to include in the token
     * @param validityMinutes  The validity period in minutes
     * @return The JWT token
     * 
     * @see TokenType
     */
    private String createToken(String subject, TokenType tokenType, Map<String, Object> additionalClaims,
            int validityMinutes) {

        logger.debug("Creating JWT token for subject [{}] with validity of {} minutes", subject, validityMinutes);

        Map<String, Object> claims = new HashMap<>(additionalClaims);
        claims.put("tokenType", tokenType.name());

        try {
            Instant now = Instant.now();
            Instant expiration = now.plus(Duration.ofMinutes(validityMinutes));

            logger.debug("Token issuance time (UTC): {}, expiration time (UTC): {}",
                    now.toString(),
                    expiration.toString());

            String token = Jwts.builder()
                    .claims(claims)
                    .subject(subject)
                    .issuer(issuer)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(expiration))
                    .signWith(getSigningKey())
                    .compact();

            logger.debug("JWT token created successfully");
            return token;
        } catch (InvalidKeyException e) {
            logger.error("Error creating JWT token for subject [{}]", subject, e);
            throw e;
        }
    }

    /**
     * Get the signing key for JWT token creation and validation.
     * 
     * @return The signing key
     */
    private SecretKey getSigningKey() {
        logger.trace("Generating signing key from secret");
        try {
            if (secretKey == null || secretKey.isEmpty()) {
                logger.error("Cannot generate signing key: Secret key is null or empty");
                throw new IllegalStateException("JWT secret key is not configured");
            }

            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            logger.trace("Signing key generated successfully");
            return key;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to decode secret key - invalid Base64 encoding", e);
            throw e;
        } catch (DecodingException | WeakKeyException | IllegalStateException e) {
            logger.error("Unexpected error while generating signing key", e);
            throw e;
        }
    }
}
