package aor.proj3t.util;

import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 * A JAX-RS filter that captures client information, establishes logging
 * context,
 * and cleans up after request processing.
 * <p>
 * This filter intercepts all incoming HTTP requests to the REST API and
 * performs the following tasks:
 * <ul>
 * <li>Extracts the client IP address from various possible request headers</li>
 * <li>Sets up the Log4j2 ThreadContext with client information</li>
 * <li>Logs details about the incoming request</li>
 * <li>Cleans up the ThreadContext after request processing completes</li>
 * </ul>
 * </p>
 * <p>
 * The filter has a high priority ({@code @Priority(1)}) to ensure it runs
 * before other filters for request processing, and after other filters for
 * response processing, ensuring proper setup and cleanup of the logging
 * context.
 * </p>
 * <p>
 * IP address extraction is handled through a sophisticated algorithm that
 * checks multiple
 * HTTP headers commonly used for forwarding client IPs in various proxy
 * configurations:
 * <ul>
 * <li>X-Forwarded-For</li>
 * <li>Proxy-Client-IP</li>
 * <li>WL-Proxy-Client-IP</li>
 * <li>HTTP_CLIENT_IP</li>
 * <li>HTTP_X_FORWARDED_FOR</li>
 * </ul>
 * If none of these headers provide a valid IP, the direct connection's remote
 * address is used.
 * </p>
 * <p>
 * For X-Forwarded-For headers that might contain multiple comma-separated IP
 * addresses,
 * only the first IP (the original client) is extracted.
 * </p>
 * 
 * @see ContainerRequestFilter
 * @see ContainerResponseFilter
 * @see ThreadContext
 */
@Provider
@Priority(1)
public class LoggingContextFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = LogManager.getLogger(LoggingContextFilter.class);
    private static final String CLIENT_IP_KEY = "clientIp";
    private static final String REQUEST_START_TIME = "requestStartTime";

    /**
     * The injected HTTP servlet request object to access HTTP headers and
     * connection information.
     */
    @Context
    private HttpServletRequest httpRequest;

    /**
     * Filters an incoming HTTP request to capture client information and establish
     * logging context.
     * <p>
     * This method is called automatically by the JAX-RS runtime for each incoming
     * request.
     * It extracts the client IP address, logs the request details, and populates
     * the Log4j2 ThreadContext with client information for use in subsequent log
     * entries.
     * </p>
     * 
     * @param requestContext The container request context containing request
     *                       information
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String clientIp = extractClientIp(httpRequest);
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();

        ThreadContext.put(CLIENT_IP_KEY, clientIp);
        ThreadContext.put(REQUEST_START_TIME, String.valueOf(System.currentTimeMillis()));

        logger.info("Request received: Method={}, Path={}, Client IP={}", method, path, clientIp);
    }

    /**
     * Filters an outgoing HTTP response to clean up logging context and log
     * response details.
     * <p>
     * This method is called automatically by the JAX-RS runtime after the response
     * has been generated but before it's sent to the client. It logs response
     * details
     * like status code and processing time, then clears the ThreadContext to
     * prevent
     * memory leaks and information leakage between requests.
     * </p>
     * 
     * @param requestContext  The container request context containing request
     *                        information
     * @param responseContext The container response context containing response
     *                        information
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        try {
            String startTimeStr = ThreadContext.get(REQUEST_START_TIME);
            if (startTimeStr != null) {
                long startTime = Long.parseLong(startTimeStr);
                long duration = System.currentTimeMillis() - startTime;

                logger.info("Response sent: Status={}, Duration={}ms",
                        responseContext.getStatus(), duration);
            }
        } finally {
            // Always clear the ThreadContext, even if logging fails
            ThreadContext.clearAll();
        }
    }

    /**
     * Extracts the client IP address from an HTTP request.
     * <p>
     * This method attempts to determine the actual client IP address by checking
     * various
     * HTTP headers that might contain forwarded IP information, falling back to the
     * direct
     * connection's remote address if no forwarding headers are found.
     * </p>
     * <p>
     * For requests passing through proxies or load balancers, this ensures the
     * original
     * client IP is identified rather than the proxy's IP.
     * </p>
     * <p>
     * If multiple IP addresses are found (e.g., in X-Forwarded-For chains), only
     * the first
     * one (which represents the original client) is returned.
     * </p>
     * 
     * @param request The HTTP servlet request to extract the client IP from
     * @return The client IP address as a string, never null
     */
    private String extractClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");

        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        // If IP contains multiple addresses (X-Forwarded-For can have a chain)
        if (clientIp != null && clientIp.contains(",")) {
            clientIp = clientIp.split(",")[0].trim();
        }

        return clientIp;
    }
}
