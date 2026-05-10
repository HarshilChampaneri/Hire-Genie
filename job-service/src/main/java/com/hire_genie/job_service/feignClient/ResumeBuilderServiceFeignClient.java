package com.hire_genie.job_service.feignClient;

import com.hire_genie.job_service.config.FeignClientConfig;
import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "resume-builder",
        url = "${services.resume-builder.url}",
        configuration = FeignClientConfig.class
)
public interface ResumeBuilderServiceFeignClient {

    @PostMapping("internal/fetch/profile")
    ProfileResponse fetchProfileResponse(@RequestBody String candidateEmail);

}
