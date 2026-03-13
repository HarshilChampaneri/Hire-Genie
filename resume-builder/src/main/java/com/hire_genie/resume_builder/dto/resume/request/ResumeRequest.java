package com.hire_genie.resume_builder.dto.resume.request;

import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponseList;
import com.hire_genie.resume_builder.dto.education.response.EducationResponseList;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponseList;
import com.hire_genie.resume_builder.dto.other.response.OtherResponse;
import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponseList;
import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;
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
