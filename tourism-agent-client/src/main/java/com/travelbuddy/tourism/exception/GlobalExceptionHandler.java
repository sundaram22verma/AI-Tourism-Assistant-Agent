package com.travelbuddy.tourism.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice 
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("Unhandled runtime context exception caught: {}", e.getMessage(), e);

        String message = e.getMessage();
        if (message != null && (message.contains("429") || message.contains("Quota exceeded") || message.contains("RESOURCE_EXHAUSTED") || message.contains("limit"))) {
            ErrorResponse response = new ErrorResponse(
                "AI Provider Rate Limit Exceeded",
                "The AI provider's rate limit or quota has been exceeded. If failover was triggered, both providers are exhausted.",
                LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
        }

        ErrorResponse response = new ErrorResponse("Internal Server Error", message, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Critical core system exception: {}", e.getMessage(), e);
        ErrorResponse response = new ErrorResponse("An unexpected error occurred", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
