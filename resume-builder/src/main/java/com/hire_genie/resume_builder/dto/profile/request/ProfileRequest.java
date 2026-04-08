package com.hire_genie.resume_builder.dto.profile.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Builder
public record ProfileRequest(

        @NotBlank
        @Length(min = 1, max = 150, message = "Full Name must be between 1 to 150 characters")
        String fullName,

        @NotBlank
        @Length(min = 1, max = 150, message = "Profession must be between 1 to 150 characters")
        String profession,

        @NotBlank(message = "Email can't be left empty")
        @Email(message = "Please enter the valid email")
        String email,

        @NotBlank
        @Length(min = 10, max = 10, message = "Mobile no. should be of exactly 10 characters")
        String mobileNo,

        @NotEmpty(message = "Profiles' url can't be left empty")
        Set<String> urls

) {
}
