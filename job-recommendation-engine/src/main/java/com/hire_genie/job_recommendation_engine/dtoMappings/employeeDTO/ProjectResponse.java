package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.YearMonth;
import java.util.List;

@Builder
public record ProjectResponse(
        Long projectId,
        String projectName,
        String projectUrl,
        List<String> projectTechStacks,
        @JsonFormat(pattern = "MM-yyyy")
        YearMonth projectStartDate,
        Boolean isProjectInProgress,
        @JsonFormat(pattern = "MM-yyyy")
        YearMonth projectEndDate,
        List<String> projectDescription
) {
}
