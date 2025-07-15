
/**
 * Exception thrown when a requested resource is not found.
 * Used to signal HTTP 404 Not Found errors in REST APIs or similar scenarios.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
package aor.proj3t.exceptions;

public class NotFoundException extends RuntimeException {
  private final String userMessage;


  /**
   * Constructs a new NotFoundException with separate user and developer messages.
   * @param userMessage the message to show to end users
   * @param devMessage the detailed message for developers/logs
   */
  public NotFoundException(String userMessage, String devMessage) {
    super(devMessage);
    this.userMessage = userMessage;
  }


  /**
   * Constructs a new NotFoundException with the same message for both user and developer.
   * @param message the message for both user and developer
   */
  public NotFoundException(String message) {
    this(message, message);
  }


  /**
   * Gets the user-friendly message.
   * @return the user message
   */
  public String getUserMessage() {
    return userMessage;
  }
}
