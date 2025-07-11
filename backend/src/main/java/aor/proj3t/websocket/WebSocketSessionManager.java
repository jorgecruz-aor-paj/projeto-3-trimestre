package aor.proj3t.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.Session;

/**
 * WebSocket Session Manager for Real-time Communication.
 * 
 * This utility class manages WebSocket sessions for users, providing
 * centralized
 * session storage and retrieval capabilities. It maintains a thread-safe
 * mapping
 * between user IDs and their active WebSocket sessions.
 * 
 * <p>
 * The manager supports operations for adding, removing, and querying user
 * sessions,
 * as well as checking connection status and broadcasting capabilities.
 * </p>
 * 
 * <p>
 * All methods are static as this class maintains global state for all WebSocket
 * connections in the application.
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see AppWebSocket
 */
public class WebSocketSessionManager {

    /**
     * Thread-safe map to store user ID to WebSocket session mapping.
     * Uses ConcurrentHashMap to handle concurrent access from multiple threads.
     */
    private static final Map<Long, Session> sessions = new ConcurrentHashMap<>();

    /**
     * Adds a user WebSocket session to the manager.
     * 
     * <p>
     * If a session already exists for the user, it will be replaced with the new
     * session.
     * This handles cases where a user reconnects or has multiple connection
     * attempts.
     * </p>
     * 
     * @param userId  the unique identifier of the user
     * @param session the WebSocket session to associate with the user
     * @throws IllegalArgumentException if userId or session is null
     */
    public static void addSession(Long userId, Session session) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null");
        }
        sessions.put(userId, session);
    }

    /**
     * Removes a user session from the manager.
     * 
     * <p>
     * This method should be called when a user disconnects or when a session
     * becomes invalid. It's safe to call this method even if no session exists
     * for the given user ID.
     * </p>
     * 
     * @param userId the unique identifier of the user whose session should be
     *               removed
     */
    public static void removeSession(Long userId) {
        sessions.remove(userId);
    }

    /**
     * Checks if a user is currently connected via WebSocket.
     * 
     * <p>
     * This method not only checks if a session exists for the user, but also
     * verifies that the session is still open and active.
     * </p>
     * 
     * @param userId the unique identifier of the user to check
     * @return true if the user has an active and open session, false otherwise
     */
    public static boolean isUserConnected(Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    /**
     * Retrieves the WebSocket session for a specific user.
     * 
     * <p>
     * Returns null if no session exists for the user. Callers should check
     * if the returned session is still open before using it.
     * </p>
     * 
     * @param userId the unique identifier of the user
     * @return the WebSocket session for the user, or null if not found
     */
    public static Session getSession(Long userId) {
        return sessions.get(userId);
    }

    /**
     * Retrieves all active user sessions.
     * 
     * <p>
     * Returns a map containing all user ID to session mappings currently
     * managed by this class. The returned map is the actual internal map,
     * so modifications will affect the session manager.
     * </p>
     * 
     * <p>
     * <strong>Warning:</strong> Use with caution as direct modifications
     * can affect the global session state.
     * </p>
     * 
     * @return an unmodifiable view of all user sessions
     */
    public static Map<Long, Session> getAllSessions() {
        return sessions;
    }
}
