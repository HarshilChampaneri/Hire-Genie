package com.hire_genie.security_service.dto;

import com.hire_genie.security_service.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.Set;

@Builder
public record RegisterRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,

        @NotEmpty
        Set<Role> roles,

        String adminSecret,

        @NotBlank
        String otp
) {
}
