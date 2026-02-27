package com.harshil_infotech.hire_genie.controller;

import com.harshil_infotech.hire_genie.dto.experience.request.ExperienceRequest;
import com.harshil_infotech.hire_genie.dto.experience.request.ExperienceRequestList;
import com.harshil_infotech.hire_genie.dto.experience.response.ExperienceResponse;
import com.harshil_infotech.hire_genie.dto.experience.response.ExperienceResponseList;
import com.harshil_infotech.hire_genie.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
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
