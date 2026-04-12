package com.hire_genie.job_recommendation_engine.controller;

import com.hire_genie.job_recommendation_engine.dtoMappings.jobDTO.JobResponse;
import com.hire_genie.job_recommendation_engine.service.JobRecommendationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs-recommendation")
@RequiredArgsConstructor
public class JobRecommendationController {

    private final JobRecommendationService jobRecommendationService;

    @GetMapping("/recommend-jobs")
    public ResponseEntity<List<JobResponse>> recommendJobs(HttpServletRequest request) {
        return ResponseEntity.ok(jobRecommendationService.recommendJobs(request));
    }

}
