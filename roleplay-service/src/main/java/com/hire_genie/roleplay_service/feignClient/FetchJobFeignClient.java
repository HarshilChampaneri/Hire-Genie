package com.hire_genie.roleplay_service.feignClient;

import com.hire_genie.roleplay_service.config.FeignClientConfig;
import com.hire_genie.roleplay_service.dto.job.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "job-service",
        url = "${services.job-service.url}",
        configuration = FeignClientConfig.class
)
public interface FetchJobFeignClient {

    @GetMapping("/internal/fetch/job/{jobId}")
    JobResponse fetchJobByJobId(
            @RequestHeader("X-Internal-Secret") String secret,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Roles") String roles,
            @PathVariable Long jobId
    );

}
