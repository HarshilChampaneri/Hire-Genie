package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.experience.request.ExperienceRequest;
import com.hire_genie.resume_builder.dto.experience.request.ExperienceRequestList;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponse;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponseList;
import com.hire_genie.resume_builder.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping("/experiences")
    public ResponseEntity<List<ExperienceResponse>> addExperiences(
            @RequestBody @Valid ExperienceRequestList experienceRequestList
    ) {
        return ResponseEntity.ok(experienceService.addExperiences(experienceRequestList));
    }

    @GetMapping("/experiences")
    public ResponseEntity<ExperienceResponseList> getAllExperiences() throws Exception {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @GetMapping("/experiences/{experienceId}")
    public ResponseEntity<ExperienceResponse> getExperienceById(@PathVariable Long experienceId) {
        return ResponseEntity.ok(experienceService.getExperienceById(experienceId));
    }

    @PutMapping("/experiences/{experienceId}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long experienceId,
            @RequestBody @Valid ExperienceRequest experienceRequest
    ) {
        return ResponseEntity.ok(experienceService.updateExperience(experienceId, experienceRequest));
    }

    @DeleteMapping("/experiences/{experienceId}")
    public ResponseEntity<String> deleteExperience(
            @PathVariable Long experienceId
    ) {
        return ResponseEntity.ok(experienceService.deleteExperience(experienceId));
    }

}
