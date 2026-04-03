package com.hire_genie.resume_builder.feignClient;

import com.hire_genie.resume_builder.dto.resume.request.ResumeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "employee-recommendation-engine", url = "${services.employee-recommendation.url}")
public interface EmployeeRecommendationEngineClient {

    @PostMapping("/internal/resume/store")
    void storeResume(@RequestBody ResumeRequest resumeRequest);

}
