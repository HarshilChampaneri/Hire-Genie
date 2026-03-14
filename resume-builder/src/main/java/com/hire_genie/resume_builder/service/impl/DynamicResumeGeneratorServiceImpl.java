package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponse;
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
import com.hire_genie.resume_builder.mapper.*;
import com.hire_genie.resume_builder.model.*;
import com.hire_genie.resume_builder.repository.*;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
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
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ProfileSummaryRepository profileSummaryRepository;
    private final ProfileSummaryMapper profileSummaryMapper;
    private final ExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;
    private final CertificateRepository certificateRepository;
    private final CertificateMapper certificateMapper;
    private final OtherRepository otherRepository;
    private final OtherMapper otherMapper;
    private final LoggedInUser loggedInUser;

    // ─── Configuration Constants ──────────────────────────────────────────────────
    private static final float MARGIN          = 50f;
    private static final float PAGE_WIDTH      = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT     = PDRectangle.A4.getHeight();
    private static final float FONT_NAME       = 20f;   // Candidate name
    private static final float FONT_PROFESSION = 14f;   // Italic profession
    private static final float FONT_SECTION    = 11f;   // Section headers
    private static final float FONT_BODY       = 10f;   // Everything else
    private static final float LINE_SPACING    = 14f;

    private PDDocument document;
    private PDPage currentPage;
    private PDPageContentStream contentStream;
    private float yPosition;
    private final DateTimeFormatter monthYearFormatter =
            DateTimeFormatter.ofPattern("MM/yyyy");

    // ─── Font Helpers ─────────────────────────────────────────────────────────────
    private PDFont fontRegular()    { return new PDType1Font(Standard14Fonts.FontName.HELVETICA); }
    private PDFont fontBold()       { return new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD); }
    private PDFont fontItalic()     { return new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE); }

    // ─── Alignment Enum ───────────────────────────────────────────────────────────
    private enum Align { LEFT, CENTER, RIGHT }

    // ─── Main Entry Point ─────────────────────────────────────────────────────────
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

        // 1. Profile
        if (profile != null) renderProfile(profile);

        // 2. Summary
        if (summary != null && summary.profileSummary() != null) {
            renderSectionHeader("PROFILE SUMMARY");
            renderParagraph(summary.profileSummary());
        }

        // 3. Experience
        if (experiences != null && experiences.experiences() != null
                && !experiences.experiences().isEmpty()) {
            renderSectionHeader("EXPERIENCE");
            for (ExperienceResponse exp : experiences.experiences()) renderExperience(exp);
        }

        // 4. Projects
        if (projects != null && projects.projects() != null
                && !projects.projects().isEmpty()) {
            renderSectionHeader("PROJECTS");
            for (ProjectResponse project : projects.projects()) renderProject(project);
        }

        // 5. Skills
        if (skills != null && skills.technicalSkills() != null
                && !skills.technicalSkills().isEmpty()) {
            renderSectionHeader("TECHNICAL SKILLS");
            renderSkills(skills.technicalSkills());
        }

        // 6. Education
        if (educations != null && educations.educations() != null
                && !educations.educations().isEmpty()) {
            renderSectionHeader("EDUCATION");
            for (EducationResponse edu : educations.educations()) renderEducation(edu);
        }

        // 7. Certificates
        if (certificates != null && certificates.certificates() != null
                && !certificates.certificates().isEmpty()) {
            renderSectionHeader("CERTIFICATES");
            for (var cert : certificates.certificates()) {
                String line = cert.certificateTitle()
                        + (cert.certificateUrl() != null ? " — " + cert.certificateUrl() : "");
                renderBulletPoint(line);
            }
        }

        // 8. Others
        if (others != null && others.description() != null
                && !others.description().isEmpty()) {
            renderSectionHeader("OTHERS");
            for (String desc : others.description()) renderBulletPoint(desc);
        }

        contentStream.close();
        document.save(outputStream);
        document.close();
    }

// ─── Section Renderers ────────────────────────────────────────────────────────

    /**
     * Header block:
     *   HARSHIL CHAMPANERI         ← bold, 20pt, centred
     *   Java Backend Developer     ← italic, 14pt, centred
     *   email | phone              ← regular, 10pt, centred
     *   links                      ← regular, 10pt, centred
     */
    @Override
    public void renderProfile(ProfileResponse p) throws IOException {
        writeLine(p.fullName(), fontBold(), FONT_NAME, Align.CENTER);
        writeLine(p.profession(), fontItalic(), FONT_PROFESSION, Align.CENTER);
        writeLine(p.email() + " | " + p.mobileNo(), fontRegular(), FONT_BODY, Align.CENTER);
        if (p.urls() != null && !p.urls().isEmpty()) {
            writeLine(String.join(" | ", p.urls()), fontRegular(), FONT_BODY, Align.CENTER);
        }
        yPosition -= 6f;
    }

    /**
     * Experience block:
     *   Position (bold, left)          08/2025 – 09/2025 (regular, right)
     *   Company Name (italic, left)
     *   • bullet …
     */
    @Override
    public void renderExperience(ExperienceResponse exp) throws IOException {
        checkAndNewPage(50f);
        String dateRange = exp.startDate().format(monthYearFormatter) + " \u2013 "
                + (exp.isWorkingInCompany() ? "Present" : exp.endDate().format(monthYearFormatter));

        writeTwoColumnLine(exp.position(),    fontBold(),    FONT_BODY,
                dateRange,         fontRegular(), FONT_BODY);
        writeLine(exp.companyName(), fontItalic(), FONT_BODY, Align.LEFT);

        if (exp.description() != null) {
            for (String bullet : exp.description()) renderBulletPoint(bullet);
        }
        yPosition -= 5f;
    }

    /**
     * Project block:
     *   Project Name (bold, left)         GitHub Link (regular, right)
     *   Tech Stack (italic, left)         11/2025 – Present (regular, right)
     *   • bullet …
     */
    @Override
    public void renderProject(ProjectResponse prj) throws IOException {
        checkAndNewPage(50f);
        String dateRange = prj.projectStartDate().format(monthYearFormatter) + " \u2013 "
                + (prj.isProjectInProgress() ? "Present" : prj.projectEndDate().format(monthYearFormatter));

        writeTwoColumnLine(prj.projectName(), fontBold(),    FONT_BODY,
                "GitHub Link",    fontRegular(), FONT_BODY);

        String techStack = (prj.projectTechStacks() != null && !prj.projectTechStacks().isEmpty())
                ? String.join(", ", prj.projectTechStacks())
                : "";
        writeTwoColumnLine(techStack, fontItalic(), FONT_BODY,
                dateRange, fontRegular(), FONT_BODY);

        for (String bullet : prj.projectDescription()) renderBulletPoint(bullet);
        yPosition -= 5f;
    }

    /**
     * Skills block (one line per category):
     *   Backend & Core:  (bold) Java, Spring Boot, … (italic)
     */
    @Override
    public void renderSkills(Map<String, List<String>> skills) throws IOException {
        for (var entry : skills.entrySet()) {
            String category   = entry.getKey() + ": ";
            String skillsList = String.join(", ", entry.getValue());
            writeMixedLine(category, fontBold(), skillsList, fontItalic(), FONT_BODY);
        }
    }

    /**
     * Education block:
     *   Gujarat Technological University (bold, left)    Gujarat, India (regular, right)
     *   Computer Engineering, B.E.       (italic, left)  07/2023 – 07/2027 (regular, right)
     *   CGPA                             (italic, left)  8.61 (regular, right)
     */
    @Override
    public void renderEducation(EducationResponse edu) throws IOException {
        checkAndNewPage(50f);
        writeTwoColumnLine(edu.educationTitle(), fontBold(),    FONT_BODY,
                edu.location(),       fontRegular(), FONT_BODY);
        writeTwoColumnLine(edu.fieldOfStudy(),   fontItalic(),  FONT_BODY,
                "",                  fontRegular(), FONT_BODY);
        writeTwoColumnLine(edu.gradeTitle(),     fontItalic(),  FONT_BODY,
                String.valueOf(edu.grades()),         fontRegular(), FONT_BODY);
        yPosition -= 5f;
    }

    /**
     * Section header with full-width underline:
     *   EXPERIENCE
     *   ──────────────────────────────────────────────────
     */
    @Override
    public void renderSectionHeader(String title) throws IOException {
        checkAndNewPage(30f);
        yPosition -= 8f;                                          // breathing space above header
        writeLine(title, fontBold(), FONT_SECTION, Align.LEFT);   // yPosition moves down by LINE_SPACING here
        contentStream.setLineWidth(0.8f);
        contentStream.moveTo(MARGIN, yPosition + LINE_SPACING - 2f);       // ← fixed: relative to text baseline
        contentStream.lineTo(PAGE_WIDTH - MARGIN, yPosition + LINE_SPACING - 2f);
        contentStream.stroke();
        yPosition -= 6f;                                          // gap between line and first content item
    }

    /** Bullet point with text wrapping and continuation indent. */
    @Override
    public void renderBulletPoint(String text) throws IOException {
        if (text == null || text.isEmpty()) return;
        float bulletIndent = MARGIN + 10f;
        float wrapWidth    = PAGE_WIDTH - bulletIndent - MARGIN - 8f;
        List<String> lines = wrapTextWithFont(text, fontRegular(), FONT_BODY, wrapWidth);

        for (int i = 0; i < lines.size(); i++) {
            checkAndNewPage(LINE_SPACING);
            contentStream.beginText();
            contentStream.setFont(fontRegular(), FONT_BODY);
            contentStream.newLineAtOffset(bulletIndent, yPosition);
            contentStream.showText((i == 0 ? "\u2022 " : "  ") + lines.get(i));
            contentStream.endText();
            yPosition -= LINE_SPACING;
        }
    }

    /** Wrapped paragraph, left-aligned. */
    @Override
    public void renderParagraph(String text) throws IOException {
        if (text == null) return;
        List<String> lines = wrapTextWithFont(text, fontRegular(), FONT_BODY,
                PAGE_WIDTH - 2f * MARGIN);
        for (String line : lines) {
            checkAndNewPage(LINE_SPACING);
            writeLine(line, fontRegular(), FONT_BODY, Align.LEFT);
        }
    }

// ─── Legacy Interface Methods (kept for interface compatibility) ───────────────

    /** Delegates to writeLine – kept for interface compatibility. */
    @Override
    public void writeText(String text, float size, boolean isBold, boolean center) throws IOException {
        writeLine(text, isBold ? fontBold() : fontRegular(), size,
                center ? Align.CENTER : Align.LEFT);
    }

    /** Delegates to wrapTextWithFont – kept for interface compatibility. */
    @Override
    public List<String> wrapText(String text, float fontSize, float width) throws IOException {
        return wrapTextWithFont(text, fontRegular(), fontSize, width);
    }

// ─── Layout Primitives ────────────────────────────────────────────────────────

    /**
     * Write a single (possibly wrapped) text block with chosen font and alignment.
     */
    private void writeLine(String text, PDFont font, float size, Align align) throws IOException {
        if (text == null || text.isEmpty()) return;
        List<String> lines = wrapTextWithFont(text, font, size, PAGE_WIDTH - 2f * MARGIN);
        for (String line : lines) {
            checkAndNewPage(LINE_SPACING);
            float textWidth = font.getStringWidth(line) / 1000f * size;
            float x = switch (align) {
                case CENTER -> (PAGE_WIDTH - textWidth) / 2f;
                case RIGHT  -> PAGE_WIDTH - MARGIN - textWidth;
                default     -> MARGIN;
            };
            contentStream.beginText();
            contentStream.setFont(font, size);
            contentStream.newLineAtOffset(x, yPosition);
            contentStream.showText(line);
            contentStream.endText();
            yPosition -= LINE_SPACING;
        }
    }

    /**
     * Two-column line: leftText anchored to MARGIN, rightText anchored to right margin.
     * If leftText wraps, subsequent lines are printed left-only (right text on first line only).
     *
     * Example:
     *   Java Full Stack Developer – Remote Internship       08/2025 – 09/2025
     */
    private void writeTwoColumnLine(String leftText,  PDFont leftFont,  float leftSize,
                                    String rightText, PDFont rightFont, float rightSize)
            throws IOException {
        if (leftText  == null) leftText  = "";
        if (rightText == null) rightText = "";

        float rightWidth = rightFont.getStringWidth(rightText) / 1000f * rightSize;
        float rightX     = PAGE_WIDTH - MARGIN - rightWidth;
        // Left column must not overlap the right column (8pt gap)
        float leftMaxWidth = rightX - MARGIN - 8f;

        List<String> leftLines = wrapTextWithFont(leftText, leftFont, leftSize, leftMaxWidth);

        // ── First line: left + right ──────────────────────────────────────────────
        checkAndNewPage(LINE_SPACING);
        if (!leftLines.get(0).isEmpty()) {
            contentStream.beginText();
            contentStream.setFont(leftFont, leftSize);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText(leftLines.get(0));
            contentStream.endText();
        }
        if (!rightText.isEmpty()) {
            contentStream.beginText();
            contentStream.setFont(rightFont, rightSize);
            contentStream.newLineAtOffset(rightX, yPosition);
            contentStream.showText(rightText);
            contentStream.endText();
        }
        yPosition -= LINE_SPACING;

        // ── Overflow left lines (no right text) ──────────────────────────────────
        for (int i = 1; i < leftLines.size(); i++) {
            checkAndNewPage(LINE_SPACING);
            contentStream.beginText();
            contentStream.setFont(leftFont, leftSize);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText(leftLines.get(i));
            contentStream.endText();
            yPosition -= LINE_SPACING;
        }
    }

    /**
     * Mixed-font line: boldText immediately followed by regularText on the same baseline.
     * Overflow of the regular text wraps to the next line, indented to where it started.
     *
     * Example:
     *   Backend & Core:  Java, Spring Boot, Spring Cloud, Spring AI, …
     */
    private void writeMixedLine(String boldText, PDFont boldFont,
                                String regularText, PDFont regularFont,
                                float size) throws IOException {
        if (boldText    == null) boldText    = "";
        if (regularText == null) regularText = "";

        checkAndNewPage(LINE_SPACING);

        float boldWidth      = boldFont.getStringWidth(boldText) / 1000f * size;
        float regularStartX  = MARGIN + boldWidth;
        float regularMaxWidth = PAGE_WIDTH - regularStartX - MARGIN;

        List<String> regularLines = wrapTextWithFont(regularText, regularFont, size, regularMaxWidth);

        // ── Bold part ─────────────────────────────────────────────────────────────
        contentStream.beginText();
        contentStream.setFont(boldFont, size);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText(boldText);
        contentStream.endText();

        // ── First regular part (same line as bold) ────────────────────────────────
        if (!regularLines.isEmpty() && !regularLines.get(0).isEmpty()) {
            contentStream.beginText();
            contentStream.setFont(regularFont, size);
            contentStream.newLineAtOffset(regularStartX, yPosition);
            contentStream.showText(regularLines.get(0));
            contentStream.endText();
        }
        yPosition -= LINE_SPACING;

        // ── Overflow regular lines ────────────────────────────────────────────────
        for (int i = 1; i < regularLines.size(); i++) {
            checkAndNewPage(LINE_SPACING);
            contentStream.beginText();
            contentStream.setFont(regularFont, size);
            contentStream.newLineAtOffset(regularStartX, yPosition);
            contentStream.showText(regularLines.get(i));
            contentStream.endText();
            yPosition -= LINE_SPACING;
        }
    }

    /**
     * Font-aware word-wrap. Returns at least one element (empty string when text is blank).
     */
    private List<String> wrapTextWithFont(String text, PDFont font, float fontSize,
                                          float maxWidth) throws IOException {
        List<String> result = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            result.add("");
            return result;
        }
        String[] words = text.split(" ", -1);
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (font.getStringWidth(candidate) / 1000f * fontSize > maxWidth && !line.isEmpty()) {
                result.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(candidate);
            }
        }
        if (!line.toString().trim().isEmpty()) result.add(line.toString().trim());
        if (result.isEmpty()) result.add("");
        return result;
    }

// ─── Page Management ──────────────────────────────────────────────────────────

    @Override
    public void checkAndNewPage(float requiredHeight) throws IOException {
        if (yPosition - requiredHeight < MARGIN) {
            contentStream.close();
            startNewPage();
        }
    }

    @Override
    public void startNewPage() throws IOException {
        currentPage   = new PDPage(PDRectangle.A4);
        document.addPage(currentPage);
        contentStream = new PDPageContentStream(document, currentPage);
        yPosition     = PAGE_HEIGHT - MARGIN;
    }

    // Resume Content Adder:
    @Override
    public ResumeRequest resumeContentAdder() {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        // Profile:-
        Profile profile = profileRepository.findExistingProfile(userEmail);

        // Profile Summary
        ProfileSummary profileSummary = profileSummaryRepository.findExistingProfileSummary(userEmail);

        // Experience
        List<Experience> experiences = experienceRepository.findActiveExperiences(userEmail);

        // Project
        List<Project> projects = projectRepository.findActiveProjects(userEmail);

        // Skill
        Skill skill = skillRepository.findActiveSkill(userEmail);

        // Education
        List<Education> educations = educationRepository.findActiveEducations(userEmail);

        // Certificate
        List<Certificate> certificates = certificateRepository.findActiveCertificates(userEmail);

        // Other
        Other other = otherRepository.findActiveOther(userEmail);

        return ResumeRequest.builder()
                .profile(fetchProfileResponse(profile))
                .summary(fetchProfileSummaryResponse(profileSummary))
                .experiences(fetchExperienceResponseList(experiences))
                .projects(fetchProjectResponseList(projects))
                .skills(fetchSkillSummaryResponse(skill))
                .educations(fetchEducationResponseList(educations))
                .certificates(fetchCertificateResponseList(certificates))
                .others(fetchOtherResponse(other))
                .build();
    }

    private ProfileResponse fetchProfileResponse(Profile profile) {
        return profile != null ? profileMapper.toProfileResponseFromProfile(profile) : null;
    }

    private ProfileSummaryResponse fetchProfileSummaryResponse(ProfileSummary profileSummary) {
        return profileSummary != null ? profileSummaryMapper.toProfileSummaryResponseFromProfileSummary(profileSummary) : null;
    }

    private ExperienceResponseList fetchExperienceResponseList(List<Experience> experiences) {

        if (experiences.isEmpty()) {
            return null;
        }

        List<ExperienceResponse> experienceResponseList = experiences.stream()
                .map(experienceMapper::toExperienceResponseFromExperience)
                .toList();

        return ExperienceResponseList.builder()
                .experiences(experienceResponseList)
                .build();
    }

    private ProjectResponseList fetchProjectResponseList(List<Project> projects) {

        if (projects.isEmpty()) {
            return null;
        }

        List<ProjectResponse> projectResponseList = projects.stream()
                .map(projectMapper::toProjectResponseFromProject)
                .toList();

        return ProjectResponseList.builder()
                .projects(projectResponseList)
                .build();
    }

    private SkillSummaryResponse fetchSkillSummaryResponse(Skill skill) {
        return skill != null ? skillMapper.toSkillResponseFromSkill(skill) : null;
    }

    private EducationResponseList fetchEducationResponseList(List<Education> educations) {

        if (educations.isEmpty()) {
            return null;
        }

        List<EducationResponse> educationResponseList = educations.stream()
                .map(educationMapper::toEducationResponseFromEducation)
                .toList();

        return EducationResponseList.builder()
                .educations(educationResponseList)
                .build();
    }

    private CertificateResponseList fetchCertificateResponseList(List<Certificate> certificates) {

        if (certificates.isEmpty()) {
            return null;
        }

        List<CertificateResponse> certificateResponseList = certificates.stream()
                .map(certificateMapper::toCertificateResponseFromCertificate)
                .toList();

        return CertificateResponseList.builder()
                .certificates(certificateResponseList)
                .build();
    }

    private OtherResponse fetchOtherResponse(Other other) {
        return other != null ? otherMapper.toOtherResponseFromOther(other) : null;
    }

}
