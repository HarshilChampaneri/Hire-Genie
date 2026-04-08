package com.hire_genie.employee_recommendation_engine.dtoMappings;

import lombok.Builder;

@Builder
public record EmployeeProfile(

        String email,
        String fullName,
        String profession

) {
}
