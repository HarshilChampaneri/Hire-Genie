package com.harshil_infotech.hire_genie.service;

import com.harshil_infotech.hire_genie.dto.response.SkillSummaryResponse;

public interface AiService {
    SkillSummaryResponse provideSkillSummary(String text);
}
