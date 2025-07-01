package aor.proj3t.dto;

public class APIResponseDTO {
    private String message;

    public APIResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
