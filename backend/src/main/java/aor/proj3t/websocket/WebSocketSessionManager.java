package aor.proj3t.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.Session;

/**
 * Manages WebSocket sessions and provides methods for broadcasting and sending
 * targeted messages.
 */
public class WebSocketSessionManager {
    // Map to store user ID to session mapping
    private static final Map<Long, Session> sessions = new ConcurrentHashMap<>();

    /**
     * Adds a user session
     * 
     * @param userId  User ID
     * @param session WebSocket session
     */
    public static void addSession(Long userId, Session session) {
        sessions.put(userId, session);
    }

    /**
     * Removes a user session
     * 
     * @param userId User ID
     */
    public static void removeSession(Long userId) {
        sessions.remove(userId);
    }

    /**
     * Checks if a user is connected
     * 
     * @param userId User ID to check
     * @return true if the user has an active session
     */
    public static boolean isUserConnected(Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    /**
     * Get the session for a specific user
     * 
     * @param userId User ID
     * @return Session for the user
     */
    public static Session getSession(Long userId) {
        return sessions.get(userId);
    }

    /**
     * Get all active sessions
     * 
     * @return Map of all user sessions
     */
    public static Map<Long, Session> getAllSessions() {
        return sessions;
    }
}
