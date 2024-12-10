package org.fna.finance.exception;

public class ExceptionResponse {
    private final String message;
    private final String timestamp;

    public ExceptionResponse(String message) {
        this.message = message;
        this.timestamp = java.time.Instant.now().toString();
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
