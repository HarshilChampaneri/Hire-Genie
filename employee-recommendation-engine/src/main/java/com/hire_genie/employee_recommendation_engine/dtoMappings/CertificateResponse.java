package com.hire_genie.employee_recommendation_engine.dtoMappings;

import lombok.Builder;

@Builder
public record CertificateResponse(
        Long certificateId,
        String certificateTitle,
        String certificateUrl
) {
}
