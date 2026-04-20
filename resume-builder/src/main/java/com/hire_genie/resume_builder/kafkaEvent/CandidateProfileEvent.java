package com.hire_genie.resume_builder.kafkaEvent;

import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import lombok.Builder;

@Builder
public record CandidateProfileEvent(
        Long jobId,
        ProfileResponse profile
) {
}
