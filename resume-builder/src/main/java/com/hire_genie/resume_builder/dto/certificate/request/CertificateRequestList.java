package com.hire_genie.resume_builder.dto.certificate.request;

import lombok.Builder;

import java.util.List;

@Builder
public record CertificateRequestList(
        List<CertificateRequest> certificateRequests
) {
}
