package com.annotation.validator.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.annotation.validator.dto.ValidationError;
import com.annotation.validator.enums.Type;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ValidationError>> handleValidationException(MethodArgumentNotValidException ex) {
	    List<ValidationError> validationErrors = new ArrayList<>();

	    ex.getBindingResult().getFieldErrors().forEach(error -> {
	        String[] parts = error.getDefaultMessage().split("\\|", 2); // Extract message and errorType
	        String message = parts[0].trim();
	        Type errorType = parts.length > 1 ? Type.valueOf(parts[1].trim()) : Type.ERROR; // Default to ERROR

	        // Find existing ValidationError for the same field & type, or create a new one
	        ValidationError existingError = validationErrors.stream()
	            .filter(err -> err.getField().equals(error.getField()) && err.getType() == errorType)
	            .findFirst()
	            .orElse(null);

	        if (existingError != null) {
	            existingError.getMessages().add(message);
	        } else {
	            validationErrors.add(new ValidationError(error.getField(), new ArrayList<>(List.of(message)), errorType));
	        }
	    });

	    return ResponseEntity.badRequest().body(validationErrors);
	}

}
