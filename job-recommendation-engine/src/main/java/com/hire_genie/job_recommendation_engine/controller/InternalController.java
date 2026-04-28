package com.hire_genie.job_recommendation_engine.controller;

import com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO.ResumeRequest;
import com.hire_genie.job_recommendation_engine.dtoMappings.jobDTO.JobResponse;
import com.hire_genie.job_recommendation_engine.service.JobRecommendationService;
import com.hire_genie.job_recommendation_engine.util.JobVectorStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final JobVectorStoreService jobVectorStoreService;
    private final JobRecommendationService jobRecommendationService;

    @PostMapping("/store/job-profile")
    public ResponseEntity<Void> storeJobProfile(@RequestBody JobResponse jobResponse) {
        jobVectorStoreService.upsertJobProfile(jobResponse);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recommend/jobs")
    public ResponseEntity<List<JobResponse>> recommendJobs(@RequestBody ResumeRequest userProfile) {

        log.info("Fetching Recommended Jobs for user with email: {}", userProfile.profile().email());
        return ResponseEntity.ok(jobRecommendationService.recommendJobs(userProfile));

    }

}
