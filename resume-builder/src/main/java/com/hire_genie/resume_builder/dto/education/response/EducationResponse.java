package com.hire_genie.resume_builder.dto.education.response;

import lombok.Builder;

import java.time.YearMonth;

@Builder
public record EducationResponse(
        Long educationId,
        String educationTitle,
        String location,
        String fieldOfStudy,
        YearMonth startDate,
        Boolean isEducationInProgress,
        YearMonth endDate,
        String gradeTitle,
        Double grades
) {
}
