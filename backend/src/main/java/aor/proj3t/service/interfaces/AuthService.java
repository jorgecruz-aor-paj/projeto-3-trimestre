package aor.proj3t.service.interfaces;

import aor.proj3t.dto.LoginDTO;

/**
 * Service interface for authentication operations.
 * This interface defines the contract for user authentication, session
 * management,
 * and security-related operations within the application.
 * 
 * <p>
 * Core authentication services include:
 * <ul>
 * <li><b>User Authentication:</b> Credential validation and login
 * processing</li>
 * <li><b>Session Management:</b> JWT token generation and session tracking</li>
 * <li><b>Logout Operations:</b> Session termination and cleanup</li>
 * <li><b>Security Integration:</b> Password verification and token
 * management</li>
 * </ul>
 * 
 * <p>
 * This service ensures secure authentication workflows with proper session
 * management, audit logging, and integration with JWT token utilities for
 * stateless authentication in the application.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
public interface AuthService {

    /**
     * Authenticates a user with the provided credentials and creates a new session.
     * Validates email and password, generates a JWT token, and creates a session
     * record.
     * 
     * @param loginDto  the login data containing email and password
     * @param ipAddress the IP address of the client making the login request
     * @param userAgent the user agent string from the client's browser
     * @return a JWT token string for the authenticated session
     * @throws UnauthorizedException if the email is not found or password is
     *                               incorrect
     */
    String login(LoginDTO loginDto, String ipAddress, String userAgent);

    /**
     * Logs out a user by invalidating their current session.
     * Marks the session as inactive to prevent further use of the token.
     * 
     * @param userId the ID of the user logging out
     * @param token  the JWT token of the session to be invalidated
     */
    void logout(Long userId, String token);

}
