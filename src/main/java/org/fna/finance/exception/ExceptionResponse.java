package org.fna.finance.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final String message;
    private final String timestamp;

    public ExceptionResponse(String message) {
        this.message = message;
        this.timestamp = java.time.Instant.now().toString();
    }

}
