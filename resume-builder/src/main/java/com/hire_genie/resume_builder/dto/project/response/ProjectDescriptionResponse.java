package com.hire_genie.resume_builder.dto.project.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectDescriptionResponse(
        List<String> projectDescription
) {
}
