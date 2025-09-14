package com.pomostudy.exception;


import com.pomostudy.dto.ErrorResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "Validation_ERROR",
                errors.toString(),
                HttpStatus.BAD_REQUEST
            );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        String errorCode = "GENERIC_DATA_INTEGRITY";
        String errorMessage = "Erro de integridade de dados.";

        String originalMessage = ex.getMessage();

        // Tenta encontrar palavras-chave na mensagem original para personalizar a resposta
        if (originalMessage.contains("duplicate key")) {
            errorCode = "DUPLICATE_KEY";
            errorMessage = "O registro que você tentou criar já existe.";
        } else {
            // Mensagem genérica se as palavras-chave não forem encontradas
            errorMessage = "Erro interno: " + originalMessage;
        }

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                errorCode,
                errorMessage,
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

}