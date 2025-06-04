package aor.proj3t.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoggingUtil {

    private final Logger logger;
    private static final String USER_ID_KEY = "userId";
    private static final String CLIENT_IP_KEY = "clientIp";

    /**
     * Default constructor for LoggingUtil.
     * Initializes the logger using Log4j2.
     */
    public LoggingUtil() {
        this.logger = LogManager.getLogger(LoggingUtil.class);
    }

    /**
     * Constructor that accepts the class that will be shown as the log source.
     * 
     * @param sourceClass The class that will be reported as the source of log
     *                    entries
     */
    public LoggingUtil(Class<?> sourceClass) {
        this.logger = LogManager.getLogger(sourceClass);
    }

    /**
     * Creates a LoggingUtil instance for the specified class.
     * 
     * @param sourceClass The class to create the logger for
     * @return A LoggingUtil instance configured for the specified class
     */
    public static LoggingUtil forClass(Class<?> sourceClass) {
        return new LoggingUtil(sourceClass);
    }

    /**
     * Log an info message.
     * 
     * @param message The message to log
     */
    public void info(String message) {
        logger.info(message);
    }

    /**
     * Log an error message.
     * 
     * @param message The message to log
     */
    public void error(String message) {
        logger.error(message);
    }

    /**
     * Log an error message with an exception.
     * 
     * @param message   The message to log
     * @param throwable The exception to log
     */
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    /**
     * Log a warning message.
     * 
     * @param message The message to log
     */
    public void warn(String message) {
        logger.warn(message);
    }

    /**
     * Log a debug message.
     * 
     * @param message The message to log
     */
    public void debug(String message) {
        logger.debug(message);
    }

    /**
     * Set the request context for logging.
     * 
     * @param userId   The ID of the authenticated user, or null if not
     *                 authenticated
     * @param clientIp The IP address of the client
     */
    public static void setRequestContext(Long userId, String clientIp) {
        ThreadContext.put(USER_ID_KEY, userId != null ? userId.toString() : "anonymous");
        ThreadContext.put(CLIENT_IP_KEY, clientIp != null ? clientIp : "unknown");
    }

    /**
     * Clear the request context for logging.
     */
    public static void clearRequestContext() {
        ThreadContext.remove(USER_ID_KEY);
        ThreadContext.remove(CLIENT_IP_KEY);
    }

    /**
     * Get the user ID from the logging context.
     * 
     * @return The user ID, or null if not set
     */
    public static Long getUserId() {
        String userId = ThreadContext.get(USER_ID_KEY);
        if (userId != null && !userId.equals("anonymous")) {
            try {
                return Long.valueOf(userId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Get the client IP from the logging context.
     * 
     * @return The client IP, or "unknown" if not set
     */
    public static String getClientIp() {
        String clientIp = ThreadContext.get(CLIENT_IP_KEY);
        return clientIp != null ? clientIp : "unknown";
    }

}
