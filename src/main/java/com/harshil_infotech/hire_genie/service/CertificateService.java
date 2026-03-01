package com.harshil_infotech.hire_genie.service;

import com.harshil_infotech.hire_genie.dto.certificate.request.CertificateRequest;
import com.harshil_infotech.hire_genie.dto.certificate.request.CertificateRequestList;
import com.harshil_infotech.hire_genie.dto.certificate.response.CertificateResponse;
import com.harshil_infotech.hire_genie.dto.certificate.response.CertificateResponseList;

import java.util.List;

public interface CertificateService {
    List<CertificateResponse> addCertificates(
            CertificateRequestList certificateRequestList
    );

    CertificateResponseList getAllCertificates() throws Exception;

    CertificateResponse getCertificateById(Long certificateId);

    CertificateResponse updateCertificate(Long certificateId, CertificateRequest certificateRequest);

    String deleteCertificate(Long certificateId);
}
