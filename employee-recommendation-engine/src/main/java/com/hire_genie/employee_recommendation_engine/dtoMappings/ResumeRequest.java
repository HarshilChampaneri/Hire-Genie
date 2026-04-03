package com.hire_genie.employee_recommendation_engine.dtoMappings;

import lombok.Builder;

@Builder
public record ResumeRequest(
        ProfileResponse profile,
        ProfileSummaryResponse summary,
        ExperienceResponseList experiences,
        ProjectResponseList projects,
        SkillSummaryResponse skills,
        EducationResponseList educations,
        CertificateResponseList certificates,
        OtherResponse others
) {
}
