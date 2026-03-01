package com.harshil_infotech.hire_genie.dto.certificate.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CertificateResponseList(
        List<CertificateResponse> certificates
) {
}
