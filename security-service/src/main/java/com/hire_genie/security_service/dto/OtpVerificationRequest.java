package com.hire_genie.security_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record OtpVerificationRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6, max = 6, message = "OTP must be exactly 6 digits.")
        String otp
) {
}
