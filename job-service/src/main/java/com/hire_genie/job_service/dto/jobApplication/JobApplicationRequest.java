package com.hire_genie.job_service.dto.jobApplication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record JobApplicationRequest(

        @NotNull(message = "Date cannot be null.")
        String interviewDate,

        @NotNull(message = "Time cannot be null.")
        String interviewTime,

        @NotNull(message = "Venue for the interview cannot be null.")
        String venue,

        @Size(max = 750)
        String additionalDetails

) {
}