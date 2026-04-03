package com.hire_genie.employee_recommendation_engine.dtoMappings;

import lombok.Builder;

import java.util.List;

@Builder
public record CertificateResponseList(
        List<CertificateResponse> certificates
) {
}
