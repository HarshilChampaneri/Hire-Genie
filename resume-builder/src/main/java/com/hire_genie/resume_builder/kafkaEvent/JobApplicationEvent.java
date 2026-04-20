package com.hire_genie.resume_builder.kafkaEvent;

import lombok.Builder;

@Builder
public record JobApplicationEvent(
        String email,
        Long jobId
) {
}
