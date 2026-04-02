package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponseList;
import com.hire_genie.resume_builder.dto.education.response.EducationResponse;
import com.hire_genie.resume_builder.dto.education.response.EducationResponseList;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponse;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponseList;
import com.hire_genie.resume_builder.dto.other.response.OtherResponse;
import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponseList;
import com.hire_genie.resume_builder.dto.resume.request.ResumeRequest;
import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface DynamicResumeGeneratorService {

    void generateResumeToStream(OutputStream outputStream,
                                ProfileResponse profile,
                                ProfileSummaryResponse summary,
                                ExperienceResponseList experiences,
                                ProjectResponseList projects,
                                SkillSummaryResponse skills,
                                EducationResponseList educations,
                                CertificateResponseList certificates,
                                OtherResponse others) throws IOException;

    void renderProfile(ProfileResponse p) throws IOException;

    void renderExperience(ExperienceResponse exp) throws IOException;

    void renderProject(ProjectResponse prj) throws IOException;

    void renderSkills(Map<String, List<String>> skills) throws IOException;

    void renderEducation(EducationResponse edu) throws IOException;

    void renderSectionHeader(String title) throws IOException;

    void renderBulletPoint(String text) throws IOException;

    void renderLinkedBulletPoint(String text, String url) throws IOException;

    void renderParagraph(String text) throws IOException;

    void writeText(String text, float size, boolean isBold, boolean center) throws IOException;

    List<String> wrapText(String text, float fontSize, float width) throws IOException;

    void checkAndNewPage(float requiredHeight) throws IOException;

    void startNewPage() throws IOException;

    // Resume Content Adder:
    ResumeRequest resumeContentAdder();

}
