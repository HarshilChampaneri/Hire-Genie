package com.hire_genie.employee_recommendation_engine.dtoMappings;

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
