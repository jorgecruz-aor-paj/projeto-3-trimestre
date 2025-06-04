package aor.proj3t.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
        try {
            // Dotenv configuration
            Dotenv dotenv = Dotenv
                    .configure()
                    .ignoreIfMalformed()
                    .load();
            // Load JWT configuration from environment variables
            secretKey = dotenv.get("JWT_SECRET");
            issuer = dotenv.get("JWT_ISSUER");
        } catch (DotenvException e) {
            logger.error("Failed to load JWT configuration from environment variables", e);
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
    public String generateToken(Long userId, String userEmail, String userRole, String ipAddress,
            Integer validityMinutes) {
        int minutes = (validityMinutes != null) ? validityMinutes : DEFAULT_TOKEN_EXPIRATION_TIME;

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", userEmail);
        claims.put("role", userRole);
        claims.put("ipAddress", ipAddress);

        return createToken(claims, userEmail, minutes);
    }

    /**
     * Create a JWT token with the specified claims.
     * 
     * @param claims          The claims to include in the token
     * @param subject         The subject of the token (typically the
     *                        username/email)
     * @param validityMinutes The validity period in minutes
     * @return The JWT token
     */
    private String createToken(Map<String, Object> claims, String subject, int validityMinutes) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityMinutes * 60 * 1000);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(issuer)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Get the signing key for JWT token creation and validation.
     * 
     * @return The signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
