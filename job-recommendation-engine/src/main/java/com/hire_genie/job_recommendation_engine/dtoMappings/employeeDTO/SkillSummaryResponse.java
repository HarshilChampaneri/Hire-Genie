package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record SkillSummaryResponse(
        Map<String, List<String>> technicalSkills
) {
}
