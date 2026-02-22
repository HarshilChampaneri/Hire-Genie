package com.harshil_infotech.hire_genie.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectResponseList(
        List<ProjectResponse> projects
) {
}
