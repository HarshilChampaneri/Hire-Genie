package com.hire_genie.roleplay_service.dto.job;

import com.hire_genie.roleplay_service.dto.job.enums.JobType;
import com.hire_genie.roleplay_service.dto.job.enums.WorkMode;

import java.math.BigDecimal;

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
