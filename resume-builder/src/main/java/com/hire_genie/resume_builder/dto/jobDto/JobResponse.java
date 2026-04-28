package com.hire_genie.resume_builder.dto.jobDto;

import lombok.Builder;
import com.hire_genie.resume_builder.enums.*;


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

