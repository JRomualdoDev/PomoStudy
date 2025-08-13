package com.pomoStudy.exception;


import com.pomoStudy.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceException(ResourceException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getCode(),
                ex.getMessage(),
                getStatusFromException(ex)
        );
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private HttpStatus getStatusFromException(ResourceException ex) {
        return switch (ex.getAction()) {
            case "not_found" -> HttpStatus.NOT_FOUND;
            case "invalid_data" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "INTERNAL_ERROR",
                "An unexpected error occurred.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}