package com.hire_genie.job_service.kafkaEvent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record JobApplicationEvent(

        @NotNull(message = "JobID cannot be null")
        Long jobId,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Please enter the valid email")
        String candidateEmail

) {
}
