package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.experience.response.ExperienceDescriptionResponse;
import com.hire_genie.resume_builder.dto.other.response.OtherDescriptionResponse;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectDescriptionResponse;
import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;
import com.hire_genie.resume_builder.prompts.user_prompts.DefaultUserSkillPrompt;
import com.hire_genie.resume_builder.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes")
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class AiController {

    private final AiService aiService;

    @PutMapping("/provide/skills/summary")
    public ResponseEntity<SkillSummaryResponse> getSkillSummary(
            @RequestParam(
                    name = "text",
                    defaultValue = DefaultUserSkillPrompt.defaultUserSkillPrompt,
                    required = false
            ) String text
    ) {
        return ResponseEntity.ok(aiService.provideSkillSummary(text));
    }

    @PutMapping("/rewirte-pd-with-ai/{projectId}")
    public ResponseEntity<ProjectDescriptionResponse> rewriteProjectDescriptionWithAI(
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(aiService.rewriteProjectDescriptionWithAi(projectId));
    }

    @PutMapping("/rewrite-experience-description-with-ai/{experienceId}")
    public ResponseEntity<ExperienceDescriptionResponse> rewriteExperienceDescriptionWithAi(
            @PathVariable Long experienceId
    ) {
        return ResponseEntity.ok(aiService.rewriteExperienceDescriptionWithAi(experienceId));
    }

    @PutMapping("/rewrite-other-section-description-with-ai/{otherId}")
    public ResponseEntity<OtherDescriptionResponse> rewriteOtherSectionDescriptionWithAi(
            @PathVariable Long otherId
    ) {
        return ResponseEntity.ok(aiService.rewriteOtherSectionDescriptionWithAi(otherId));
    }

    @PutMapping("/rewrite-profile-summary-with-ai")
    public ResponseEntity<ProfileSummaryResponse> rewriteProfileSummaryWithAi() throws Exception {
        return ResponseEntity.ok(aiService.rewriteProfileSummaryWithAi());
    }

    @PostMapping("/generate-profile-summary-with-ai")
    public ResponseEntity<ProfileSummaryResponse> generateProfileSummaryWithAi() throws Exception {
        return ResponseEntity.ok(aiService.generateProfileSummaryWithAi());
    }

}
