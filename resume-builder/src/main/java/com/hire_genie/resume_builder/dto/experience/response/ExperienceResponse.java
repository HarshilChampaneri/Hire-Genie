package com.hire_genie.resume_builder.dto.experience.response;

import lombok.Builder;

import java.time.YearMonth;
import java.util.List;

@Builder
public record ExperienceResponse(
        Long experienceId,
        String companyName,
        String position,
        YearMonth startDate,
        Boolean isWorkingInCompany,
        YearMonth endDate,
        List<String> description
) {
}
