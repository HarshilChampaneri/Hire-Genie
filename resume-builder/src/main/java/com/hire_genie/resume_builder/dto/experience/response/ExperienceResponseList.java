package com.hire_genie.resume_builder.dto.experience.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ExperienceResponseList(
        List<ExperienceResponse> experiences
) {
}
