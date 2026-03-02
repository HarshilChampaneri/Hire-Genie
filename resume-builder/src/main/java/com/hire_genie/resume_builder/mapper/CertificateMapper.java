package com.hire_genie.resume_builder.mapper;

import com.hire_genie.resume_builder.dto.certificate.request.CertificateRequest;
import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponse;
import com.hire_genie.resume_builder.model.Certificate;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    public Certificate toCertificateFromCertificateRequest(CertificateRequest certificateRequest) {
        return Certificate.builder()
                .certificateTitle(certificateRequest.certificateTitle() != null ? certificateRequest.certificateTitle() : null)
                .certificateUrl(certificateRequest.certificateUrl() != null ? certificateRequest.certificateUrl() : null)
                .build();
    }

    public CertificateResponse toCertificateResponseFromCertificate(Certificate certificate) {
        return CertificateResponse.builder()
                .certificateId(certificate.getCertificateId())
                .certificateTitle(certificate.getCertificateTitle())
                .certificateUrl(certificate.getCertificateUrl())
                .build();
    }
}
