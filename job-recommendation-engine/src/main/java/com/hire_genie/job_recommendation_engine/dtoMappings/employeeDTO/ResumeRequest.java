package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

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
