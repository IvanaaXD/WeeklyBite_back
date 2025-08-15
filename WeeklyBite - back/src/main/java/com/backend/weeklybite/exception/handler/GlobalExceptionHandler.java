package com.backend.weeklybite.exception.handler;

import com.backend.weeklybite.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handles Bean Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = null;
            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            } else {
                fieldName = error.getObjectName();
            }
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    // Handles specific custom exceptions
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
//        return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
//                .body("{\"message\": \"" + ex.getMessage() + "\"}");
//    }
//
//    @ExceptionHandler(FileStorageException.class)
//    public ResponseEntity<String> handleFileStorageException(FileStorageException ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
//                .body("{\"message\": \"" + ex.getMessage() + "\"}");
//    }
//
//    @ExceptionHandler(EmailServiceException.class)
//    public ResponseEntity<String> handleEmailServiceException(EmailServiceException ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
//                .body("{\"message\": \"" + ex.getMessage() + "\"}");
//    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404 Not Found
                .body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    // NEWLY ADDED: Handles when a resource (like EventType or Category) is not found
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404 Not Found
//                .body("{\"message\": \"" + ex.getMessage() + "\"}");
//    }

    // If you introduce InvalidDataException for semantic validation errors
//    @ExceptionHandler(InvalidDataException.class)
//    public ResponseEntity<String> handleInvalidDataException(InvalidDataException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400 Bad Request
//                .body("{\"message\": \"" + ex.getMessage() + "\"}");
//    }
//
//    // Handles Spring Security's AccessDeniedException
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN) // 403 Forbidden
//                .body("{\"message\": \"" + ex.getMessage() + "\"}");
//    }
//
//    // Handles Spring Security's UsernameNotFoundException (e.g., during login)
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404 Not Found
//                .body("{\"message\": \"" + ex.getMessage() + "\"}");
//    }

    // Handles generic IllegalArgumentException, useful if you throw it for invalid input not caught by @Valid
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400 Bad Request
                .body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    // Handles NoSuchElementException (general "not found" scenario if not more specific UserNotFoundException applies)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404 Not Found
                .body("{\"message\": \"" + ex.getMessage() + "\"}");
    }

    // Generic Exception Handler (catches any other unhandled exceptions)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Always log unhandled exceptions for debugging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body("{\"message\": \"An unexpected error occurred: " + ex.getMessage() + "\"}");
    }
    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(org.springframework.web.server.ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body("{\"message\": \"" + ex.getReason() + "\"}");
    }
}
