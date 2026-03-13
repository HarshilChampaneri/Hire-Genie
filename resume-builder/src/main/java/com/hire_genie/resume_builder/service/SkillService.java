package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;

public interface SkillService {
    SkillSummaryResponse getSkills() throws Exception;
}
