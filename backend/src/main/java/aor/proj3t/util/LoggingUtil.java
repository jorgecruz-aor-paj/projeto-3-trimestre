package aor.proj3t.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Utility class for centralized application logging using Log4j2.
 * <p>
 * This class provides a standardized interface for logging operations across
 * the application,
 * with methods for different log levels and contextual information management.
 * It leverages Log4j2's ThreadContext to maintain request-scoped logging
 * context
 * for tracking user sessions and client information.
 * </p>
 * <p>
 * Usage examples:
 * 
 * <pre>{@code
 * // Instance for the current class
 * LoggingUtil logger = LoggingUtil.forClass(MyClass.class);
 * 
 * // Set request context at the beginning of a request
 * LoggingUtil.setRequestContext(userId, clientIp);
 * 
 * // Log messages at different levels
 * logger.info("User profile updated successfully");
 * logger.debug("Processing data: " + data);
 * 
 * // Log exceptions with context
 * try {
 *     // Some operation
 * } catch (Exception e) {
 *     logger.error("Failed to process transaction", e);
 * }
 * 
 * // Clear context at the end of a request
 * LoggingUtil.clearRequestContext();
 * }</pre>
 * </p>
 * 
 * @see org.apache.logging.log4j.Logger
 * @see org.apache.logging.log4j.ThreadContext
 */
@ApplicationScoped
public class LoggingUtil {

    private final Logger logger;
    private static final String USER_ID_KEY = "userId";
    private static final String CLIENT_IP_KEY = "clientIp";

    /**
     * Default constructor that initializes the logger for the LoggingUtil class
     * itself.
     * <p>
     * This constructor is primarily used when the logging source doesn't need
     * to be attributed to a specific class.
     * </p>
     */
    public LoggingUtil() {
        this.logger = LogManager.getLogger(LoggingUtil.class);
    }

    /**
     * Constructor that initializes the logger for a specific source class.
     * <p>
     * Use this constructor when you want logs to be attributed to a particular
     * class,
     * which helps with filtering and identifying the source of log entries.
     * </p>
     * 
     * @param sourceClass The class that will be reported as the source of log
     *                    entries
     */
    public LoggingUtil(Class<?> sourceClass) {
        this.logger = LogManager.getLogger(sourceClass);
    }

    /**
     * Factory method to create a LoggingUtil instance for a specific class.
     * <p>
     * This is the recommended way to obtain a LoggingUtil instance as it
     * properly attributes logs to the calling class.
     * </p>
     * 
     * @param sourceClass The class to create the logger for
     * @return A LoggingUtil instance configured for the specified class
     */
    public static LoggingUtil forClass(Class<?> sourceClass) {
        return new LoggingUtil(sourceClass);
    }

    /**
     * Logs a message at the INFO level.
     * <p>
     * Use this method for general operational information that highlights
     * the progress of the application.
     * </p>
     * 
     * @param message The message to log
     */
    public void info(String message) {
        logger.info(message);
    }

    /**
     * Logs a message at the ERROR level.
     * <p>
     * Use this method for error events that might still allow the application
     * to continue running, but require attention.
     * </p>
     * 
     * @param message The message to log
     */
    public void error(String message) {
        logger.error(message);
    }

    /**
     * Logs a message with an exception at the ERROR level.
     * <p>
     * Use this method when catching exceptions to provide both an error message
     * and the full stack trace for debugging purposes.
     * </p>
     * 
     * @param message   The message to log
     * @param throwable The exception to log
     */
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    /**
     * Logs a message at the WARN level.
     * <p>
     * Use this method for potentially harmful situations that don't prevent
     * normal operation but may indicate problems or unexpected behavior.
     * </p>
     * 
     * @param message The message to log
     */
    public void warn(String message) {
        logger.warn(message);
    }

    /**
     * Logs a message at the DEBUG level.
     * <p>
     * Use this method for detailed information useful for debugging.
     * These messages are typically disabled in production.
     * </p>
     * 
     * @param message The message to log
     */
    public void debug(String message) {
        logger.debug(message);
    }

    /**
     * Sets the request context for the current thread to enable correlation of log
     * entries.
     * <p>
     * This method should be called at the beginning of processing each request,
     * typically in a filter or interceptor, to establish context for all subsequent
     * logging operations within the same thread.
     * </p>
     * 
     * @param userId   The ID of the authenticated user, or null if not
     *                 authenticated
     * @param clientIp The IP address of the client making the request
     */
    public static void setRequestContext(Long userId, String clientIp) {
        ThreadContext.put(USER_ID_KEY, userId != null ? userId.toString() : "anonymous");
        ThreadContext.put(CLIENT_IP_KEY, clientIp != null ? clientIp : "unknown");
    }

    /**
     * Clears the request context from the current thread.
     * <p>
     * This method should be called at the end of request processing to prevent
     * context information from leaking into subsequent requests handled by the same
     * thread.
     * It's important to call this method in a finally block or filter's
     * afterCompletion
     * method to ensure the context is always cleared.
     * </p>
     */
    public static void clearRequestContext() {
        ThreadContext.remove(USER_ID_KEY);
        ThreadContext.remove(CLIENT_IP_KEY);
    }

    /**
     * Retrieves the user ID from the current thread's logging context.
     * <p>
     * This method allows access to the user ID that was previously set via
     * {@link #setRequestContext(Long, String)} for the current thread.
     * </p>
     * 
     * @return The user ID as a Long, or null if not set or if the value is
     *         "anonymous"
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
     * Retrieves the client IP address from the current thread's logging context.
     * <p>
     * This method allows access to the client IP that was previously set via
     * {@link #setRequestContext(Long, String)} for the current thread.
     * </p>
     * 
     * @return The client IP address as a String, or "unknown" if not set
     */
    public static String getClientIp() {
        String clientIp = ThreadContext.get(CLIENT_IP_KEY);
        return clientIp != null ? clientIp : "unknown";
    }
}
