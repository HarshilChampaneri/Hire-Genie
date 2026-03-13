package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;
import com.hire_genie.resume_builder.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/get-skill")
    public ResponseEntity<SkillSummaryResponse> getSkills() throws Exception {
        return ResponseEntity.ok(skillService.getSkills());
    }

}
