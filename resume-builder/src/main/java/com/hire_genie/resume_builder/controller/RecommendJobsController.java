package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.jobDto.JobResponse;
import com.hire_genie.resume_builder.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class RecommendJobsController {

    private final RecommendJobsService recommendJobsService;

    @GetMapping("/recommend/jobs")
    public ResponseEntity<List<JobResponse>> fetchUserProfileAndRecommendJobs() {
        return ResponseEntity.ok(recommendJobsService.fetchProfileAndRecommendJobs());
    }

}
