package com.hire_genie.job_recommendation_engine.controller;

import com.hire_genie.job_recommendation_engine.dtoMappings.jobDTO.JobResponse;
import com.hire_genie.job_recommendation_engine.util.JobVectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final JobVectorStoreService jobVectorStoreService;

    @PostMapping("/store/job-profile")
    public ResponseEntity<Void> storeJobProfile(JobResponse jobResponse) {
        jobVectorStoreService.upsertJobProfile(jobResponse);
        return ResponseEntity.ok().build();
    }

}
