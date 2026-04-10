package com.hire_genie.employee_recommendation_engine.feignClient;

import com.hire_genie.employee_recommendation_engine.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "job-service",
        url = "${services.job-service.url}",
        configuration = FeignClientConfig.class
)
public interface JobServiceFeignClient {

    @GetMapping("/internal/fetch/job-description/{jobId}")
    String fetchJobDescription(
            @RequestHeader("X-Internal-Secret") String secret,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Roles") String roles,
            @PathVariable Long jobId
    );

}
