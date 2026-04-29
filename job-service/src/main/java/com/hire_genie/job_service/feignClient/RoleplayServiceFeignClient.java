package com.hire_genie.job_service.feignClient;

import com.hire_genie.job_service.config.FeignClientConfig;
import com.hire_genie.job_service.dto.roleplay.RoleplayDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "roleplay-service",
        url = "${services.roleplay-service.url}",
        configuration = FeignClientConfig.class
)
public interface RoleplayServiceFeignClient {

    @PostMapping("/internal/start/roleplay")
    RoleplayDTO startRoleplay(@RequestBody String jobDescription);

}
