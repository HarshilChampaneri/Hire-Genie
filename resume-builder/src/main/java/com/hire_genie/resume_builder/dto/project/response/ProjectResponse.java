package com.hire_genie.resume_builder.dto.project.response;

import lombok.Builder;

import java.time.YearMonth;
import java.util.List;

@Builder
public record ProjectResponse(
        Long projectId,
        String projectName,
        String projectUrl,
        List<String> projectTechStacks,
        YearMonth projectStartDate,
        Boolean isProjectInProgress,
        YearMonth projectEndDate,
        List<String> projectDescription
) {
}
