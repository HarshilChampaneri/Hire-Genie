package com.hire_genie.job_service.dto.job.response;

import lombok.Builder;

@Builder
public record JobResponse(
        Long jobId,
        String jobName,
        String jobDescription
) {
}
