package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.jobDto.*;
import com.hire_genie.resume_builder.dto.resume.request.ResumeRequest;
import com.hire_genie.resume_builder.service.DynamicResumeGeneratorService;
import com.hire_genie.resume_builder.service.*;
import com.hire_genie.resume_builder.feignClient.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendJobsServiceImpl implements RecommendJobsService {

    private final JobRecommendationFeignClient jobRecommendationFeignClient;
    private final DynamicResumeGeneratorService resumeGeneratorService;

    @Override
    public List<JobResponse> fetchProfileAndRecommendJobs() {
        ResumeRequest userProfile = resumeGeneratorService.resumeContentAdder();

        log.info("User Profile for Email: {} fetched Successfully.", userProfile.profile().email());
        return jobRecommendationFeignClient.recommendJobs(userProfile);
    }

}
