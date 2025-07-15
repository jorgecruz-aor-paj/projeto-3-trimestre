package aor.proj3t.util.authutils;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import aor.proj3t.enums.TokenType;
import aor.proj3t.exceptions.JwtConfigurationException;
import aor.proj3t.exceptions.JwtTokenException;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Utility class for handling JWT (JSON Web Tokens) operations.
 * 
 * This class provides methods for generating and validating JWT tokens
 * used for authentication and authorization throughout the application.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    private static final int DEFAULT_TOKEN_EXPIRATION_TIME = AppConstants.Jwt.DEFAULT_TOKEN_EXPIRATION_TIME;
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
                logger.error("JWT_SECRET is not set in environment variables or .env file");
                throw new JwtConfigurationException(
                        "Authentication service temporarily unavailable",
                        "JWT secret key is not configured");
            }
            if (issuer == null || issuer.isEmpty()) {
                logger.error("JWT_ISSUER is not set in environment variables or .env file");
                throw new JwtConfigurationException(
                        "Authentication service temporarily unavailable",
                        "JWT issuer is not configured");
            }

            logger.info("JWT utility service initialized successfully");
        } catch (DotenvException e) {
            logger.error("Failed to load JWT configuration from environment variables or .env file", e);
            throw new JwtConfigurationException(
                    "Authentication service temporarily unavailable",
                    "Failed to load JWT configuration: " + e.getMessage());
        } catch (JwtConfigurationException e) {
            throw e; // Re-throw our custom exceptions
        } catch (RuntimeException e) {
            logger.error("Runtime error during JWT utility initialization", e);
            throw new JwtConfigurationException(
                    "Authentication service temporarily unavailable",
                    "Runtime error during JWT initialization: " + e.getMessage());
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
        } catch (JwtConfigurationException e) {
            logger.error("Failed to generate JWT token for user [ID={}] due to configuration error", userId, e);
            throw e;
        } catch (JwtTokenException e) {
            logger.error("Failed to generate JWT token for user [ID={}] due to token error", userId, e);
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
        } catch (JwtConfigurationException e) {
            logger.error("Failed to generate JWT token for email [EMAIL={}] due to configuration error", userEmail, e);
            throw e;
        } catch (JwtTokenException e) {
            logger.error("Failed to generate JWT token for email [EMAIL={}] due to token error", userEmail, e);
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
    public String generatePasswordResetToken(String userEmail, Long userId, String ipAddress, Integer validityMinutes) {
        int minutes = (validityMinutes != null) ? validityMinutes : DEFAULT_TOKEN_EXPIRATION_TIME;

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userEmail);
        claims.put("ipAddress", ipAddress);
        claims.put("userId", userId);

        try {
            String token = createToken(userEmail, TokenType.PASSWORD_RESET, claims, minutes);

            logger.info("JWT token generated successfully for email [EMAIL={}]", userEmail);
            return token;
        } catch (JwtConfigurationException e) {
            logger.error("Failed to generate JWT token for email [EMAIL={}] due to configuration error", userEmail, e);
            throw e;
        } catch (JwtTokenException e) {
            logger.error("Failed to generate JWT token for email [EMAIL={}] due to token error", userEmail, e);
            throw e;
        }
    }

    /**
     * Generate a temporary token for two-factor authentication during login.
     * This token has limited scope and can only be used to complete 2FA
     * verification.
     * 
     * @param userId    The user ID requiring 2FA
     * @param userEmail The user email
     * @param ipAddress The IP address of the client
     * @return The temporary JWT token for 2FA completion
     */
    public String generateTwoFactorLoginToken(Long userId, String userEmail, String ipAddress) {
        // 2FA tokens should have a very short expiration time
        int validityMinutes = AppConstants.Jwt.TWO_FACTOR_LOGIN_EXPIRATION_TIME;

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", userEmail);
        claims.put("ipAddress", ipAddress);

        try {
            String token = createToken(userEmail, TokenType.TWO_FACTOR_LOGIN, claims, validityMinutes);
            logger.info("Temporary 2FA token generated successfully for user [ID={}]", userId);
            return token;
        } catch (JwtConfigurationException e) {
            logger.error("Failed to generate 2FA token for user [ID={}] due to configuration error", userId, e);
            throw e;
        } catch (JwtTokenException e) {
            logger.error("Failed to generate 2FA token for user [ID={}] due to token error", userId, e);
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
                    .notBefore(Date.from(now.minusSeconds(60))) // Allow a 60 seconds grace period
                    .expiration(Date.from(expiration))
                    .signWith(getSigningKey())
                    .compact();

            logger.debug("JWT token created successfully");
            return token;
        } catch (InvalidKeyException e) {
            logger.error("Error creating JWT token for subject [{}] - invalid signing key", subject, e);
            throw new JwtConfigurationException(
                    "Authentication service temporarily unavailable",
                    "Invalid JWT signing key configuration: " + e.getMessage());
        } catch (JwtConfigurationException e) {
            throw e; // Re-throw our configuration exceptions
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument for JWT token creation for subject [{}]", subject, e);
            throw new JwtTokenException(
                    "Failed to create authentication token",
                    "Invalid token parameters: " + e.getMessage());
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
                throw new JwtConfigurationException(
                        "Authentication service temporarily unavailable",
                        "JWT secret key is not configured");
            }

            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            logger.trace("Signing key generated successfully");
            return key;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to decode secret key - invalid Base64 encoding", e);
            throw new JwtConfigurationException(
                    "Authentication service temporarily unavailable",
                    "Invalid JWT secret key format - must be valid Base64: " + e.getMessage());
        } catch (DecodingException e) {
            logger.error("Failed to decode secret key", e);
            throw new JwtConfigurationException(
                    "Authentication service temporarily unavailable",
                    "Failed to decode JWT secret key: " + e.getMessage());
        } catch (WeakKeyException e) {
            logger.error("JWT secret key is too weak", e);
            throw new JwtConfigurationException(
                    "Authentication service temporarily unavailable",
                    "JWT secret key is too weak - must be at least 256 bits: " + e.getMessage());
        } catch (JwtConfigurationException e) {
            throw e; // Re-throw our custom exceptions
        }
    }

    /**
     * Validate a JWT token.
     * 
     * @param token The JWT token
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                logger.debug("Token validation failed: token is null or empty");
                return false;
            }

            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            logger.debug("Token validation failed: invalid signature - {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            logger.debug("Token validation failed: JWT error - {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.debug("Token validation failed: illegal argument - {}", e.getMessage());
            return false;
        } catch (JwtConfigurationException e) {
            logger.error("Token validation failed due to configuration error", e);
            throw e; // Re-throw configuration errors as they indicate system issues
        }
    }

    /**
     * Extract all claims from a JWT token.
     * 
     * @param token The JWT token
     * @return The claims
     * @throws JwtTokenException if the token is invalid or cannot be parsed
     */
    public Claims extractAllClaims(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new JwtTokenException(
                        "Invalid token",
                        "Token is null or empty");
            }

            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            logger.debug("Failed to extract claims: invalid token signature - {}", e.getMessage());
            throw new JwtTokenException(
                    "Invalid token",
                    "Token signature verification failed: " + e.getMessage());
        } catch (JwtException e) {
            logger.debug("Failed to extract claims: JWT error - {}", e.getMessage());
            throw new JwtTokenException(
                    "Invalid token",
                    "Token parsing failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.debug("Failed to extract claims: illegal argument - {}", e.getMessage());
            throw new JwtTokenException(
                    "Invalid token",
                    "Token format is invalid: " + e.getMessage());
        } catch (JwtConfigurationException e) {
            logger.error("Failed to extract claims due to configuration error", e);
            throw e; // Re-throw configuration errors
        } catch (JwtTokenException e) {
            throw e; // Re-throw our custom token exceptions
        }
    }

    /**
     * Extract the user ID from a JWT token.
     * 
     * @param token The JWT token
     * @return The user ID
     * @throws JwtTokenException if the token is invalid or the user ID is not found
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);
        if (userId == null) {
            throw new JwtTokenException(
                    "Invalid token",
                    "Token does not contain user ID claim");
        }
        return userId;
    }

    /**
     * Extract the user email from a JWT token.
     * 
     * @param token The JWT token
     * @return The user email
     * @throws JwtTokenException if the token is invalid or the email is not found
     */
    public String extractUserEmail(String token) {
        Claims claims = extractAllClaims(token);
        String email = claims.getSubject();
        if (email == null || email.trim().isEmpty()) {
            throw new JwtTokenException(
                    "Invalid token",
                    "Token does not contain email subject");
        }
        return email;
    }

    /**
     * Extract the user role from a JWT token.
     * 
     * @param token The JWT token
     * @return The user role
     * @throws JwtTokenException if the token is invalid or the role is not found
     */
    public String extractUserRole(String token) {
        Claims claims = extractAllClaims(token);
        String role = claims.get("role", String.class);
        if (role == null || role.trim().isEmpty()) {
            throw new JwtTokenException(
                    "Invalid token",
                    "Token does not contain role claim");
        }
        return role;
    }

    /**
     * Extract the token expiration date from a JWT token.
     * 
     * @param token The JWT token
     * @return The expiration date as Instant
     * @throws JwtTokenException if the token is invalid or the expiration date is
     *                           not found
     */
    public Instant extractExpirationDate(String token) {
        Claims claims = extractAllClaims(token);
        Date expirationDate = claims.getExpiration();
        if (expirationDate == null) {
            throw new JwtTokenException(
                    "Invalid token",
                    "Token does not contain expiration claim");
        }
        return expirationDate.toInstant();
    }

    /**
     * Check if a JWT token is expired.
     * 
     * @param token The JWT token
     * @return true if the token is expired, false otherwise
     * @throws JwtTokenException if the token is invalid
     */
    public boolean isTokenExpired(String token) {
        Instant expirationDate = extractExpirationDate(token);
        return expirationDate.isBefore(Instant.now());
    }

    /**
     * Extract the token type from a JWT token.
     * 
     * @param token The JWT token
     * @return The token type
     * @throws JwtTokenException if the token is invalid or the token type is not
     *                           found
     */
    public String extractTokenType(String token) {
        Claims claims = extractAllClaims(token);
        String tokenType = claims.get("tokenType", String.class);
        if (tokenType == null || tokenType.trim().isEmpty()) {
            throw new JwtTokenException(
                    "Invalid token",
                    "Token does not contain token type claim");
        }
        return tokenType;
    }

    /**
     * Extract an optional claim from a JWT token without throwing an exception if
     * not found.
     * 
     * @param token     The JWT token
     * @param claimName The name of the claim to extract
     * @param claimType The expected type of the claim
     * @return The claim value, or null if not found
     * @throws JwtTokenException if the token is invalid
     */
    public <T> T extractOptionalClaim(String token, String claimName, Class<T> claimType) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName, claimType);
    }

    /**
     * Validate that a token is of the expected type.
     * 
     * @param token        The JWT token
     * @param expectedType The expected token type
     * @return true if the token is of the expected type
     * @throws JwtTokenException if the token is invalid or not of the expected type
     */
    public boolean validateTokenType(String token, TokenType expectedType) {
        String tokenType = extractTokenType(token);
        boolean isValidType = expectedType.name().equals(tokenType);

        if (!isValidType) {
            throw new JwtTokenException(
                    "Invalid token type",
                    String.format("Expected token type '%s' but found '%s'", expectedType.name(), tokenType));
        }

        return true;
    }

}
