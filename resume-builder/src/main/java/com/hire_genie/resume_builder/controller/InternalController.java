package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import com.hire_genie.resume_builder.service.InternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final InternalService internalService;

    @PostMapping("/fetch/profile")
    public ResponseEntity<ProfileResponse> fetchProfileResponse(@RequestBody String candidateEmail) throws Exception {
        return ResponseEntity.ok(internalService.fetchProfileResponse(candidateEmail));
    }

}
