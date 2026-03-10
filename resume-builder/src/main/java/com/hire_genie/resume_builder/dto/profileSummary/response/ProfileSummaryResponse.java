package com.hire_genie.resume_builder.dto.profileSummary.response;

import lombok.Builder;

@Builder
public record ProfileSummaryResponse(
        Long profileSummaryId,
        String profileSummary
) {
}
