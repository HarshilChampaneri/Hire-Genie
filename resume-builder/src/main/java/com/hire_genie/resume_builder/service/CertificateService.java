package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.certificate.request.CertificateRequest;
import com.hire_genie.resume_builder.dto.certificate.request.CertificateRequestList;
import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponse;
import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponseList;

import java.util.List;

public interface CertificateService {
    List<CertificateResponse> addCertificates(CertificateRequestList certificateRequestList);

    CertificateResponseList getAllCertificates() throws Exception;

    CertificateResponse getCertificateById(Long certificateId);

    CertificateResponse updateCertificate(Long certificateId, CertificateRequest certificateRequest);

    String deleteCertificate(Long certificateId);
}
