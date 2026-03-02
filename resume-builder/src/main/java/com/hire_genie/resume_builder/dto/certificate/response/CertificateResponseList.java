package com.hire_genie.resume_builder.dto.certificate.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CertificateResponseList(
        List<CertificateResponse> certificates
) {
}
