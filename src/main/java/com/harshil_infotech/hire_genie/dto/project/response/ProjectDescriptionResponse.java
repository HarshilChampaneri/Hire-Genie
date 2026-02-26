package com.harshil_infotech.hire_genie.dto.project.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectDescriptionResponse(
        List<String> projectDescription
) {
}
