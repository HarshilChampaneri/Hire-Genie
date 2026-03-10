package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.profile.request.ProfileRequest;
import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import com.hire_genie.resume_builder.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/profile")
    public ResponseEntity<ProfileResponse> addNewProfile(
            @RequestBody @Valid ProfileRequest profileRequest
    ) throws Exception {
        return ResponseEntity.ok(profileService.addNewProfile(profileRequest));
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getYourProfile() throws Exception {
        return ResponseEntity.ok(profileService.getYourProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody @Valid ProfileRequest profileRequest
    ) throws Exception {
        return ResponseEntity.ok(profileService.updateYourProfile(profileRequest));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteProfile() throws Exception {
        return ResponseEntity.ok(profileService.deleteYourProfile());
    }

}
