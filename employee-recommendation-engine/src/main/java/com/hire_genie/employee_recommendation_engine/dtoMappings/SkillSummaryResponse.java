package com.hire_genie.employee_recommendation_engine.dtoMappings;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record SkillSummaryResponse(
        Map<String, List<String>> technicalSkills
) {
}
