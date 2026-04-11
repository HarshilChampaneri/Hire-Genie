package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import lombok.Builder;

import java.util.Set;

@Builder
public record ProfileResponse(

        Long profileId,
        String fullName,
        String profession,
        String email,
        String mobileNo,
        Set<String> urls

) {
}
