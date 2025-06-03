package aor.proj3t.service.interfaces;

import java.util.List;
import java.util.Optional;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.entities.Session;

public interface AuthService {

    /**
     * Authenticates a user and creates a session.
     *
     * @param loginDTO  the login credentials
     * @param userAgent the user agent of the client
     * @param ipAddress the IP address of the client
     * @return a SessionEntity representing the authenticated session
     */
    Session login(LoginDTO loginDTO, String userAgent, String ipAddress);

    /**
     * Logs out a user by invalidating their session.
     *
     * @param sessionToken the session token of the user to log out
     * @return true if the logout was successful, false otherwise
     */
    boolean logout(String sessionToken);

    /**
     * Logout a user from all devices.
     * 
     * @param userId The ID of the user
     * @return The number of sessions that were invalidated
     */
    int logoutAll(Long userId);

    /**
     * Get a session by its token.
     * 
     * @param token The session token
     * @return An Optional containing the session if found, or empty if not found
     */
    Optional<Session> getSession(String token);

    /**
     * Get all active sessions for a user.
     * 
     * @param userId The ID of the user
     * @return A list of active sessions for the user
     */
    List<Session> getActiveSessions(Long userId);

    /**
     * Clean up expired sessions.
     * 
     * @return The number of sessions that were cleaned up
     */
    int cleanupExpiredSessions();
}
