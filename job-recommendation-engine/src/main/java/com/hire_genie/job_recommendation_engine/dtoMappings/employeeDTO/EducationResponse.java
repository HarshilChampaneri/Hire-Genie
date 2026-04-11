package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.YearMonth;

@Builder
public record EducationResponse(
        Long educationId,
        String educationTitle,
        String location,
        String fieldOfStudy,
        @JsonFormat(pattern = "MM-yyyy")
        YearMonth startDate,
        Boolean isEducationInProgress,
        @JsonFormat(pattern = "MM-yyyy")
        YearMonth endDate,
        String gradeTitle,
        Double grades
) {
}
