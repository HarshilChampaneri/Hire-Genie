package com.hire_genie.job_service.dto.job.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record JobRequest(

        @NotBlank
        @Size(min = 1, max = 150, message = "Length of the job name should be between 1 to 150 characters.")
        String jobName,

        @NotBlank
        @Size(min = 1, max = 1000, message = "Length of the job description should be between 1 to 1000 characters.")
        String jobDescription

) {
}
