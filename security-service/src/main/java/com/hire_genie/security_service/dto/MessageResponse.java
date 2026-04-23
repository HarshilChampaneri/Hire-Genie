package com.hire_genie.security_service.dto;

import lombok.Builder;

@Builder
public record MessageResponse(
        String message
) {
}
