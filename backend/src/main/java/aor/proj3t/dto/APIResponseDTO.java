package aor.proj3t.dto;

/**
 * Data Transfer Object representing a generic API response.
 * <p>
 * Typically used to return a simple message in JSON format to indicate
 * success, failure, or status feedback from REST endpoints. Promotes consistency
 * in response payloads across the application.
 * </p>
 *
 * Example usage:
 * <pre>{@code
 *     return Response.ok(new APIResponseDTO("Operation completed successfully")).build();
 * }</pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class APIResponseDTO {

    private String message;

    /**
     * Constructs the response DTO with a message.
     *
     * @param message the response message to convey
     */
    public APIResponseDTO(String message) {
        this.message = message;
    }

    /**
     * Retrieves the response message.
     *
     * @return the message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets or updates the response message.
     *
     * @param message the new message value
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

