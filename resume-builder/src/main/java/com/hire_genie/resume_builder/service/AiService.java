package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.experience.response.ExperienceDescriptionResponse;
import com.hire_genie.resume_builder.dto.other.response.OtherDescriptionResponse;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectDescriptionResponse;
import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;

public interface AiService {
    SkillSummaryResponse provideSkillSummary(String text);

    ProjectDescriptionResponse rewriteProjectDescriptionWithAi(Long projectId);

    ExperienceDescriptionResponse rewriteExperienceDescriptionWithAi(Long experienceId);

    OtherDescriptionResponse rewriteOtherSectionDescriptionWithAi(Long otherId);

    ProfileSummaryResponse rewriteProfileSummaryWithAi() throws Exception;

    ProfileSummaryResponse generateProfileSummaryWithAi() throws Exception;
}
