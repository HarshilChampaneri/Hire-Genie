package com.hire_genie.job_recommendation_engine.dtoMappings.jobDTO;

import com.hire_genie.job_recommendation_engine.enums.JobType;
import com.hire_genie.job_recommendation_engine.enums.WorkMode;
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
