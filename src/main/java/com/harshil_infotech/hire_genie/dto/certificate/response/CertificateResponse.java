package com.harshil_infotech.hire_genie.dto.certificate.response;

import lombok.Builder;

@Builder
public record CertificateResponse(
        Long certificateId,
        String certificateTitle,
        String certificateUrl
) {
}
