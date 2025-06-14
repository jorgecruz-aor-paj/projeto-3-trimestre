package aor.proj3t.exceptions;

public class NotFoundException extends RuntimeException {
  private final String userMessage;

  public NotFoundException(String userMessage, String devMessage) {
    super(devMessage);
    this.userMessage = userMessage;
  }

  public NotFoundException(String message) {
    this(message, message);
  }

  public String getUserMessage() {
    return userMessage;
  }
}
