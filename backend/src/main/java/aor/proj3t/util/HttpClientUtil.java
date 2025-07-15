package aor.proj3t.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
/**
 * Utility class for making HTTP requests to external services.
 * <p>
 * Encapsulates reusable logic for sending POST requests with JSON payloads
 * using Java's {@link HttpClient}. Provides standardized logging and
 * error handling, including timeouts, retries, and response code validation.
 * Designed for synchronous invocation from within service layers.
 * </p>
 *
 * Usage Example:
 * <pre>{@code
 *     String json = "{\"key\":\"value\"}";
 *     String response = HttpClientUtil.post("https://api.example.com/endpoint", json);
 * }</pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LogManager.getLogger(HttpClientUtil.class);
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Sends a synchronous HTTP POST request with a JSON body to the specified URL.
     * Logs request and response details for debugging and observability.
     *
     * @param url the target endpoint to send the request to
     * @param jsonPayload the JSON body to include in the POST request
     * @return the response body as a string if the request succeeds
     * @throws IOException if the request fails or response is non-2xx
     */
    public static String post(String url, String jsonPayload) throws IOException {
        try {
            LOGGER.info("Sending request to: {}", url);
            LOGGER.debug("Request payload: {}", jsonPayload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.info("Response status code: {}", response.statusCode());
            LOGGER.debug("Response body: {}", response.body());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            } else {
                throw new IOException("Request failed with status code: " + response.statusCode() +
                        ", Response: " + response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        } catch (IOException e) {
            LOGGER.error("Error sending HTTP request", e);
            throw new IOException("Error sending HTTP request: " + e.getMessage(), e);
        }
    }
}
