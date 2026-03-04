package com.hire_genie.security_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
