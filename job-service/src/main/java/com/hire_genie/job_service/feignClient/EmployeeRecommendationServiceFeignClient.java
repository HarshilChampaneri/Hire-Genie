package com.hire_genie.job_service.feignClient;

import com.hire_genie.job_service.config.FeignClientConfig;
import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "employee-recommendation-engine",
        url = "${services.employee-recommendation-engine.url}",
        configuration = FeignClientConfig.class
)
public interface EmployeeRecommendationServiceFeignClient {

    @PostMapping("/internal/recommend/employees")
    List<ProfileResponse> recommendEmployees(@RequestBody String jobDescription);

}
