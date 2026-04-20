package com.hire_genie.job_service.kafkaEvent;

import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import lombok.Builder;

@Builder
public record CandidateProfileEvent(
        Long jobId,
        ProfileResponse profile
) {
}
