package aor.proj3t.service.interfaces;

import aor.proj3t.entities.Session;
import aor.proj3t.enums.Role;

/**
 * Service interface for JWT token management and validation operations.
 * This interface defines the contract for token-based authentication, session
 * management,
 * and secure extraction of user information from JWT tokens.
 * 
 * <p>
 * Core token services include:
 * <ul>
 * <li><b>Token Validation:</b> JWT token authenticity and expiration
 * verification</li>
 * <li><b>User Information Extraction:</b> Secure extraction of user ID and role
 * from tokens</li>
 * <li><b>Session Management:</b> Token-to-session mapping and validation</li>
 * <li><b>Authorization Header Processing:</b> Bearer token parsing and
 * validation</li>
 * <li><b>Session Status Tracking:</b> Active session verification and
 * management</li>
 * </ul>
 * 
 * <p>
 * This service provides the foundation for stateless authentication in the
 * application,
 * integrating with JWT utilities and session management to ensure secure token
 * operations.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
public interface TokenService {

    /**
     * Extracts the user ID from a JWT token after validating its authenticity and
     * session status.
     * 
     * @param token the JWT token string to extract the user ID from
     * @return the user ID contained in the token
     * @throws UnauthorizedException if the token is invalid or session is inactive
     */
    Long getUserIdFromToken(String token);

    /**
     * Extracts the user ID from an Authorization header containing a Bearer token.
     * Parses the header, extracts the token, and validates it.
     * 
     * @param authHeader the Authorization header value (e.g., "Bearer <token>")
     * @return the user ID contained in the token
     * @throws UnauthorizedException if the token is invalid or session is inactive
     */
    Long getUserIdFromAuthHeader(String authHeader);

    /**
     * Extracts the user role from an Authorization header containing a Bearer
     * token.
     * Parses the header, extracts the token, validates it, and returns the user's
     * role.
     * 
     * @param authHeader the Authorization header value (e.g., "Bearer <token>")
     * @return the Role enum value representing the user's role
     * @throws UnauthorizedException if the token is invalid or session is inactive
     */
    Role getRoleFromAuthHeader(String authHeader);

    /**
     * Checks if a JWT token is associated with an active session.
     * Validates the token against the session database to ensure the session hasn't
     * been terminated.
     * 
     * @param token the JWT token to check for active session status
     * @return true if the token is associated with an active session, false
     *         otherwise
     */
    boolean isTokenActive(String token);

    /**
     * Retrieves the session entity associated with a JWT token.
     * Looks up the session in the database using the token value.
     * 
     * @param token the JWT token to look up the session for
     * @return the Session entity associated with the token
     * @throws NotFoundException if no session is found for the given token
     */
    Session getSessionByToken(String token);

}
