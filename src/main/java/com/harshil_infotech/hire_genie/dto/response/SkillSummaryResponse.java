package com.harshil_infotech.hire_genie.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record SkillSummaryResponse(
        Map<String, List<String>> technicalSkills,
        List<String> domainExpertise,
        List<String> softSkills,
        String experienceLevel
) {
}
