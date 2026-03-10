package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.profileSummary.request.ProfileSummaryRequest;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;
import com.hire_genie.resume_builder.service.ProfileService;
import com.hire_genie.resume_builder.service.ProfileSummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class ProfileSummaryController {

    private final ProfileSummaryService profileSummaryService;

    @PostMapping("/profile-summary")
    public ResponseEntity<ProfileSummaryResponse> addNewProfileSummary(@RequestBody @Valid ProfileSummaryRequest profileSummaryRequest) throws Exception {
        return ResponseEntity.ok(profileSummaryService.addNewProfileSummary(profileSummaryRequest));
    }

    @GetMapping("/profile-summary")
    public ResponseEntity<ProfileSummaryResponse> getYourProfileSummary() throws Exception {
        return ResponseEntity.ok(profileSummaryService.getYourProfileSummary());
    }

    @PutMapping("/profile-summary")
    public ResponseEntity<ProfileSummaryResponse> updateYourProfileSummary(@RequestBody @Valid ProfileSummaryRequest profileSummaryRequest) throws Exception {
        return ResponseEntity.ok(profileSummaryService.updateProfileSummary(profileSummaryRequest));
    }

    @DeleteMapping("/profile-summary")
    public ResponseEntity<String> deleteProfileSummary() {
        return ResponseEntity.ok(profileSummaryService.deleteProfileSummary());
    }

}
