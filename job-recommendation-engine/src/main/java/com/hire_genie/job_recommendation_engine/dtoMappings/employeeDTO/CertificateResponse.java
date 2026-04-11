package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import lombok.Builder;

@Builder
public record CertificateResponse(
        Long certificateId,
        String certificateTitle,
        String certificateUrl
) {
}
