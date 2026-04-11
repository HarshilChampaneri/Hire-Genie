package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import lombok.Builder;

@Builder
public record EmployeeProfile(

        String email,
        String fullName,
        String profession

) {
}
