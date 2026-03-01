package com.harshil_infotech.hire_genie.dto.certificate.request;

import lombok.Builder;

import java.util.List;

@Builder
public record CertificateRequestList(
        List<CertificateRequest> certificateRequests
) {
}
