package com.harshil_infotech.hire_genie.mapper;

import com.harshil_infotech.hire_genie.dto.certificate.request.CertificateRequest;
import com.harshil_infotech.hire_genie.dto.certificate.response.CertificateResponse;
import com.harshil_infotech.hire_genie.model.Certificate;
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
