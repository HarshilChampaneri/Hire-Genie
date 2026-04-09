package com.hire_genie.job_service.dto.job.request;

import com.hire_genie.job_service.enums.JobType;
import com.hire_genie.job_service.enums.WorkMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record JobRequest(

        @NotBlank
        @Size(min = 1, max = 150, message = "Length of the job name should be between 1 to 150 characters.")
        String jobTitle,

        @NotBlank
        @Size(min = 1, max = 1000, message = "Length of the job description should be between 1 to 1000 characters.")
        String jobDescription,

        @NotBlank
        JobType jobType,

        @NotBlank
        WorkMode workMode,

        @NotBlank
        @Size(min = 1, max = 100, message = "Location cannot be empty.")
        String location,

        BigDecimal minSalary,
        BigDecimal maxSalary,
        String currency,

        @NotBlank
        Integer vacancies

) {
}
