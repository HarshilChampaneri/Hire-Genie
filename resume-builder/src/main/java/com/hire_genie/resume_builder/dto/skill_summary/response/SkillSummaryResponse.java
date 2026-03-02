package com.hire_genie.resume_builder.dto.skill_summary.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record SkillSummaryResponse(
        Map<String, List<String>> technicalSkills
) {
}
