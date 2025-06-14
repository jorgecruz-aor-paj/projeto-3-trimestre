package aor.proj3t.util.authutils;

import aor.proj3t.config.AppConstants;
import aor.proj3t.exceptions.UnauthorizedException;

/**
 * Utility class for token-related operations
 */
public class TokenUtils {

    /**
     * Extracts token from Authorization header
     * 
     * @param authHeader Authorization header
     * @return The extracted token
     * @throws UnauthorizedException if the header is missing or invalid
     */
    public static String extractToken(String authHeader) {
        if (!hasToken(authHeader)) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        return authHeader.substring(AppConstants.Jwt.AUTHENTICATION_SCHEME.length()).trim();
    }

    /**
     * Validates that a token is present in the Authorization header
     * 
     * @param authHeader Authorization header
     * @return true if the token is present, false otherwise
     */
    public static boolean hasToken(String authHeader) {
        return authHeader != null && authHeader.startsWith(AppConstants.Jwt.AUTHENTICATION_SCHEME + " ");
    }

    /**
     * Gets the authentication scheme used
     * 
     * @return The authentication scheme (e.g., "Bearer")
     */
    public static String getAuthenticationScheme() {
        return AppConstants.Jwt.AUTHENTICATION_SCHEME;
    }
}