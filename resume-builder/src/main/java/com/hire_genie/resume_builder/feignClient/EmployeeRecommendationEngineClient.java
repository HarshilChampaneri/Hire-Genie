package com.hire_genie.resume_builder.feignClient;

import com.hire_genie.resume_builder.config.FeignClientConfig;
import com.hire_genie.resume_builder.dto.resume.request.ResumeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "employee-recommendation-engine",
        url = "${services.employee-recommendation.url}",
        configuration = FeignClientConfig.class
)
public interface EmployeeRecommendationEngineClient {

    @PostMapping("/internal/resume/store")
    void storeResume(
            @RequestHeader("X-Internal-Secret") String secret,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Roles") String roles,
            @RequestBody ResumeRequest resumeRequest
    );
}
