package com.hire_genie.employee_recommendation_engine.controller;

import com.hire_genie.employee_recommendation_engine.dtoMappings.ResumeRequest;
import com.hire_genie.employee_recommendation_engine.service.ResumeVectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/resume")
public class ResumeIngestionController {

    private final ResumeVectorStoreService resumeVectorStoreService;

    @PostMapping("/store")
    public ResponseEntity<Void> storeResume(@RequestBody ResumeRequest resumeRequest) {
        resumeVectorStoreService.upsertResume(resumeRequest);
        return ResponseEntity.ok().build();
    }

}
