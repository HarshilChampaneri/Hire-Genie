package com.hire_genie.employee_recommendation_engine.dtoMappings;

import lombok.Builder;

@Builder
public record ProfileSummaryResponse(
        Long profileSummaryId,
        String profileSummary
) {
}
