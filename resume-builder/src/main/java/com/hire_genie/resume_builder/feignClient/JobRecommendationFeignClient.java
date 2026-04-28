package com.hire_genie.resume_builder.feignClient;

import com.hire_genie.resume_builder.config.FeignClientConfig;
import com.hire_genie.resume_builder.dto.jobDto.*;
import com.hire_genie.resume_builder.dto.resume.request.ResumeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "job-recommendation-engine",
        url = "${services.job-recommendation-engine.url}",
        configuration = FeignClientConfig.class
)
public interface JobRecommendationFeignClient {

    @PostMapping("/internal/recommend/jobs")
    List<JobResponse> recommendJobs(@RequestBody ResumeRequest userProfile);

}
