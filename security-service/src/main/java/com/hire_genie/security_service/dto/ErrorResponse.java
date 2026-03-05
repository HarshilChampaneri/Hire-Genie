package com.hire_genie.security_service.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ErrorResponse(
        Integer status,
        String message,
        String path,
        LocalDateTime timestamp,
        Map<String, String> validationErrors
) {
}
