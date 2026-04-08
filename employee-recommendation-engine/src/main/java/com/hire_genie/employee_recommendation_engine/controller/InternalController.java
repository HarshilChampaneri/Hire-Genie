package com.hire_genie.employee_recommendation_engine.controller;

import com.hire_genie.employee_recommendation_engine.dtoMappings.ResumeRequest;
import com.hire_genie.employee_recommendation_engine.service.ResumeVectorStoreService;
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

    private final ResumeVectorStoreService resumeVectorStoreService;

    @PostMapping("resume/store")
    public ResponseEntity<Void> storeResume(@RequestBody ResumeRequest resumeRequest) {
        resumeVectorStoreService.upsertResume(resumeRequest);
        return ResponseEntity.ok().build();
    }

}
