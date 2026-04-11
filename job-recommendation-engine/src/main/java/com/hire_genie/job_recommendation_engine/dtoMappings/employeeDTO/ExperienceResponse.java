package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.YearMonth;
import java.util.List;

@Builder
public record ExperienceResponse(
        Long experienceId,
        String companyName,
        String position,
        @JsonFormat(pattern = "MM-yyyy")
        YearMonth startDate,
        Boolean isWorkingInCompany,
        @JsonFormat(pattern = "MM-yyyy")
        YearMonth endDate,
        List<String> description
) {
}
