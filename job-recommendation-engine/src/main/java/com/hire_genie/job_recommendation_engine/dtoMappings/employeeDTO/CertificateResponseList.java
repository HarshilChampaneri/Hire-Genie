package com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO;

import lombok.Builder;

import java.util.List;

@Builder
public record CertificateResponseList(
        List<CertificateResponse> certificates
) {
}
