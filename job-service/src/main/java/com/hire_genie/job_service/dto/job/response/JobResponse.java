package com.hire_genie.job_service.dto.job.response;

import com.hire_genie.job_service.enums.JobType;
import com.hire_genie.job_service.enums.WorkMode;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record JobResponse(
        Long jobId,
        String jobTitle,
        String jobDescription,
        JobType jobType,
        WorkMode workMode,
        String location,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        String currency,
        Integer vacancies
) {
}
