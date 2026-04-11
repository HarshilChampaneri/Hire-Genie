package com.hire_genie.job_recommendation_engine.feignClient;

import com.hire_genie.job_recommendation_engine.config.FeignClientConfig;
import com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO.ResumeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "resume-builder",
        url = "${services.resume-builder.url}",
        configuration = FeignClientConfig.class
)
public interface UserProfileFeignClient {

    @GetMapping("/internal/fetch/user-profile")
    ResumeRequest fetchUserProfile(
            @RequestHeader("X-Internal-Secret") String secret,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Roles") String roles
    );

}
