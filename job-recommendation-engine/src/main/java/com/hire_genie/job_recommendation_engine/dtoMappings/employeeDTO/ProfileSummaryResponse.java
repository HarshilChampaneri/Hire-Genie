package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import lombok.Builder;

@Builder
public record ProfileSummaryResponse(
        Long profileSummaryId,
        String profileSummary
) {
}
