package com.hire_genie.job_service.dto.jobApplication;

import lombok.Builder;

@Builder
public record JobApplicationResponse(

        Long id,
        String candidateEmail,
        String recruiterEmail,
        String companyName,
        String jobTitle

) {
}
