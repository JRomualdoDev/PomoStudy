package com.pomostudy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private final String code;
    private final String message;
    private final int status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public ErrorResponseDTO(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status.value();
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}