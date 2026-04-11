package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.resume.request.ResumeRequest;
import com.hire_genie.resume_builder.service.impl.DynamicResumeGeneratorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final DynamicResumeGeneratorServiceImpl resumeGeneratorService;

    @GetMapping("/fetch/user-profile")
    public ResponseEntity<ResumeRequest> fetchUserProfile(){
        return ResponseEntity.ok(resumeGeneratorService.resumeContentAdder());
    }

}
