package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.jobDto.JobResponse;

import java.util.List;

public interface RecommendJobsService {
    List<JobResponse> fetchProfileAndRecommendJobs();
}
