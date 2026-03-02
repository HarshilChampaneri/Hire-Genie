package com.hire_genie.resume_builder.dto.certificate.response;

import lombok.Builder;

@Builder
public record CertificateResponse(
        Long certificateId,
        String certificateTitle,
        String certificateUrl
) {
}
