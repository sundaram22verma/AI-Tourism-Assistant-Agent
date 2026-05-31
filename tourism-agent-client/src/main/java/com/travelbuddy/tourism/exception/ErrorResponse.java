package com.travelbuddy.tourism.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String details;
    private LocalDateTime timestamp;
}
