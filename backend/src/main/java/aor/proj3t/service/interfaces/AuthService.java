package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.entities.Session;

public interface AuthService {

    String login(LoginDTO loginDto, String ipAddress, String userAgent);

    void logout(Long userId, String token);

    /**
     * Logout a user from all devices.
     * 
     * @param userId The ID of the user
     * @return The number of sessions that were invalidated
     */
    int logoutAll(Long userId);


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

    Boolean isValidToken(String token);

    Long getUserIdFromAuthHeader(String authHeader);
    Long getUserIdFromToken(String token);


    }
