package com.poc.profilehub.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.poc.profilehub.dto.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
          //      LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "User Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex,HttpServletRequest request) {
	    ErrorResponse error = new ErrorResponse(
	    	//	LocalDateTime.now(),
	        HttpStatus.CONFLICT.value(),
	        "User Alreay Exists with email id",
	        ex.getMessage(),
	        request.getRequestURI()
	        
	    );
	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // You can customize the message here or parse ex.getMessage() if needed
        String message = "Data integrity violation: possibly duplicate key or constraint violation";

        ErrorResponse errorResponse = new ErrorResponse(
        //    LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Conflict",
            message,
            "none"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + ex.getMessage());
    }
    
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(
            ExpiredJwtException e, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpServletResponse.SC_UNAUTHORIZED,
            "JWT_EXPIRED",
            "JWT token has expired",
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(errorResponse);
    }
}
