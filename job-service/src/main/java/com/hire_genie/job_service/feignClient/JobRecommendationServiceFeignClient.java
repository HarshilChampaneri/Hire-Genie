package com.hire_genie.job_service.feignClient;

import com.hire_genie.job_service.config.FeignClientConfig;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "job-recommendation-service",
        url = "${services.job-recommendation.url}",
        configuration = FeignClientConfig.class
)
public interface JobRecommendationServiceFeignClient {

    @PostMapping("/internal/store/job-profile")
    void storeJobProfile(
            @RequestHeader("X-Internal-Secret") String secret,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Roles") String roles,
            @RequestBody JobResponse jobResponse
    );

}

