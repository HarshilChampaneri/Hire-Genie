package com.hire_genie.security_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegistrationOtpRequest(
        @NotBlank
        @Email
        String email
) {
}
