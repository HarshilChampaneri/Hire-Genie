package com.hire_genie.resume_builder.service.impl;

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
import com.hire_genie.resume_builder.model.ProfileSummary;
import com.hire_genie.resume_builder.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DynamicResumeGeneratorServiceImpl implements DynamicResumeGeneratorService {

    // Dependencies
    private final ProfileService profileService;
    private final ProfileSummary profileSummary;
    private final ExperienceService experienceService;
    private final ProjectService projectService;
    private final SkillService skillService;
    private final EducationService educationService;
    private final CertificateService certificateService;
    private final OtherService otherService;

    // Configuration Constants
    private static final float MARGIN = 50;
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private static final float FONT_SIZE_TITLE = 16;
    private static final float FONT_SIZE_SUBTITLE = 12;
    private static final float FONT_SIZE_BODY = 10;
    private static final float LINE_SPACING = 15;

    private PDDocument document;
    private PDPage currentPage;
    private PDPageContentStream contentStream;
    private float yPosition;
    private final DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

    @Override
    public void generateResumeToStream(OutputStream outputStream,
                                       ProfileResponse profile,
                                       ProfileSummaryResponse summary,
                                       ExperienceResponseList experiences,
                                       ProjectResponseList projects,
                                       SkillSummaryResponse skills,
                                       EducationResponseList educations,
                                       CertificateResponseList certificates,
                                       OtherResponse others) throws IOException {

        document = new PDDocument();
        startNewPage();

        // 1. Profile Section
        if (profile != null) renderProfile(profile);

        // 2. Summary Section
        if (summary != null && summary.profileSummary() != null) {
            renderSectionHeader("PROFILE SUMMARY");
            renderParagraph(summary.profileSummary());
        }

        // 3. Experience Section
        if (experiences != null && experiences.experiences() != null && !experiences.experiences().isEmpty()) {
            renderSectionHeader("EXPERIENCE");
            for (ExperienceResponse exp : experiences.experiences()) {
                renderExperience(exp);
            }
        }

        // 4. Projects Section
        if (projects != null && projects.projects() != null && !projects.projects().isEmpty()) {
            renderSectionHeader("PROJECTS");
            for (ProjectResponse project : projects.projects()) {
                renderProject(project);
            }
        }

        // 5. Skills Section
        if (skills != null && skills.technicalSkills() != null && !skills.technicalSkills().isEmpty()) {
            renderSectionHeader("TECHNICAL SKILLS");
            renderSkills(skills.technicalSkills());
        }

        // 6. Education Section
        if (educations != null && educations.educations() != null && !educations.educations().isEmpty()) {
            renderSectionHeader("EDUCATION");
            for (EducationResponse edu : educations.educations()) {
                renderEducation(edu);
            }
        }

        // 7. Certificates Section
        if (certificates != null && certificates.certificates() != null && !certificates.certificates().isEmpty()) {
            renderSectionHeader("CERTIFICATES");
            for (var cert : certificates.certificates()) {
                renderBulletPoint(cert.certificateTitle() + (cert.certificateUrl() != null ? " - " + cert.certificateUrl() : ""));
            }
        }

        // 8. Others Section
        if (others != null && others.description() != null && !others.description().isEmpty()) {
            renderSectionHeader("OTHERS");
            for (String desc : others.description()) {
                renderBulletPoint(desc);
            }
        }

        contentStream.close();
        document.save(outputStream);
        document.close();
    }

    // --- RENDER HELPERS ---

    @Override
    public void renderProfile(ProfileResponse p) throws IOException {
        writeText(p.fullName(), FONT_SIZE_TITLE, true, true);
        writeText(p.profession(), FONT_SIZE_SUBTITLE, false, true);

        String contactInfo = p.email() + " | " + p.mobileNo();
        writeText(contactInfo, FONT_SIZE_BODY, false, true);

        if (p.urls() != null) {
            writeText(String.join(" | ", p.urls()), FONT_SIZE_BODY, false, true);
        }
        yPosition -= 10;
    }

    @Override
    public void renderExperience(ExperienceResponse exp) throws IOException {
        checkAndNewPage(40);
        String dateRange = exp.startDate().format(monthYearFormatter) + " - " +
                (exp.isWorkingInCompany() ? "Present" : exp.endDate().format(monthYearFormatter));

        writeText(exp.companyName() + " (" + dateRange + ")", FONT_SIZE_SUBTITLE, true, false);
        writeText(exp.position(), FONT_SIZE_BODY, false, true);

        if (exp.description() != null) {
            for (String bullet : exp.description()) {
                renderBulletPoint(bullet);
            }
        }
        yPosition -= 5;
    }

    @Override
    public void renderProject(ProjectResponse prj) throws IOException {
        checkAndNewPage(40);
        String dateRange = prj.projectStartDate().format(monthYearFormatter) + " - " +
                (prj.isProjectInProgress() ? "Present" : prj.projectEndDate().format(monthYearFormatter));

        writeText(prj.projectName() + " | " + dateRange, FONT_SIZE_SUBTITLE, true, false);
        if (prj.projectTechStacks() != null) {
            writeText("Tech Stack: " + String.join(", ", prj.projectTechStacks()), FONT_SIZE_BODY, false, true);
        }

        for (String bullet : prj.projectDescription()) {
            renderBulletPoint(bullet);
        }
        yPosition -= 5;
    }

    @Override
    public void renderSkills(Map<String, List<String>> skills) throws IOException {
        for (var entry : skills.entrySet()) {
            String skillLine = entry.getKey() + ": " + String.join(", ", entry.getValue());
            renderParagraph(skillLine);
        }
    }

    @Override
    public void renderEducation(EducationResponse edu) throws IOException {
        checkAndNewPage(30);
        writeText(edu.educationTitle() + " - " + edu.fieldOfStudy(), FONT_SIZE_SUBTITLE, true, false);
        writeText(edu.location() + " | " + edu.gradeTitle() + ": " + edu.grades(), FONT_SIZE_BODY, false, true);
        yPosition -= 5;
    }

    // --- LAYOUT ENGINE ENGINE ---

    @Override
    public void renderSectionHeader(String title) throws IOException {
        checkAndNewPage(30);
        yPosition -= 10;
        writeText(title, FONT_SIZE_SUBTITLE, true, false);
        // Draw underline
        contentStream.setLineWidth(1f);
        contentStream.moveTo(MARGIN, yPosition + 2);
        contentStream.lineTo(PAGE_WIDTH - MARGIN, yPosition + 2);
        contentStream.stroke();
        yPosition -= 5;
    }

    @Override
    public void renderBulletPoint(String text) throws IOException {
        checkAndNewPage(15);
        float bulletMargin = MARGIN + 10;
        List<String> lines = wrapText(text, FONT_SIZE_BODY, PAGE_WIDTH - bulletMargin - MARGIN);

        for (int i = 0; i < lines.size(); i++) {
            checkAndNewPage(15);
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_BODY);
            contentStream.newLineAtOffset(bulletMargin, yPosition);
            contentStream.showText((i == 0 ? "• " : "  ") + lines.get(i));
            contentStream.endText();
            yPosition -= LINE_SPACING;
        }
    }

    @Override
    public void renderParagraph(String text) throws IOException {
        List<String> lines = wrapText(text, FONT_SIZE_BODY, PAGE_WIDTH - (2 * MARGIN));
        for (String line : lines) {
            checkAndNewPage(15);
            writeText(line, FONT_SIZE_BODY, false, true);
        }
    }

    @Override
    public void writeText(String text, float size, boolean isBold, boolean center) throws IOException {
        if (text == null) return;
        checkAndNewPage(15);
        contentStream.beginText();
        var font = isBold ? new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD) : new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        contentStream.setFont(font, size);

        float textWidth = font.getStringWidth(text) / 1000 * size;
        float x = center ? (PAGE_WIDTH - textWidth) / 2 : MARGIN;

        contentStream.newLineAtOffset(x, yPosition);
        contentStream.showText(text);
        contentStream.endText();
        yPosition -= LINE_SPACING;
    }

    @Override
    public List<String> wrapText(String text, float fontSize, float width) throws IOException {
        List<String> result = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        for (String word : words) {
            if (font.getStringWidth(line + word) / 1000 * fontSize > width) {
                result.add(line.toString().trim());
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }
        result.add(line.toString().trim());
        return result;
    }

    @Override
    public void checkAndNewPage(float requiredHeight) throws IOException {
        if (yPosition - requiredHeight < MARGIN) {
            contentStream.close();
            startNewPage();
        }
    }

    @Override
    public void startNewPage() throws IOException {
        currentPage = new PDPage(PDRectangle.A4);
        document.addPage(currentPage);
        contentStream = new PDPageContentStream(document, currentPage);
        yPosition = PAGE_HEIGHT - MARGIN;
    }

    // Resume Content Adder:

    public ResumeRequest resumeContentAdder() {
        return ResumeRequest.builder().build();
    }

}
