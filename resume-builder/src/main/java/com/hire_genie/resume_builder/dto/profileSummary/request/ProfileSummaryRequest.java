package com.hire_genie.resume_builder.dto.profileSummary.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record ProfileSummaryRequest(

        @NotBlank
        @Length(max = 350, message = "Profile summary should not exceed 350 characters.")
        String profileSummary

) {
}
