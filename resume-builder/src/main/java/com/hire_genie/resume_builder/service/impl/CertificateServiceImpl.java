package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.certificate.request.CertificateRequest;
import com.hire_genie.resume_builder.dto.certificate.request.CertificateRequestList;
import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponse;
import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponseList;
import com.hire_genie.resume_builder.exception.ResourceNotFoundException;
import com.hire_genie.resume_builder.mapper.CertificateMapper;
import com.hire_genie.resume_builder.model.Certificate;
import com.hire_genie.resume_builder.repository.CertificateRepository;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final CertificateMapper certificateMapper;
    private final LoggedInUser loggedInUser;

    @Override
    public List<CertificateResponse> addCertificates(
            CertificateRequestList certificateRequestList
    ) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        List<Certificate> certificates = certificateRequestList
                .certificateRequests()
                .stream()
                .map(certificateMapper::toCertificateFromCertificateRequest)
                .toList();

        for (Certificate certificate : certificates) {
            certificate.setIsCertificateDeleted(false);
            certificate.setUserEmail(userEmail);
        }

        return certificateRepository.saveAll(certificates)
                .stream()
                .map(certificateMapper::toCertificateResponseFromCertificate)
                .toList();

    }

    @Override
    public CertificateResponseList getAllCertificates() throws Exception {

        List<Certificate> certificates = certificateRepository.findActiveCertificates(loggedInUser.getCurrentLoggedInUser());

        if (certificates.isEmpty()) {
            throw new Exception("No certificates Found");
        }

        List<CertificateResponse> certificateResponses = certificates
                .stream()
//                .filter(c -> Boolean.FALSE.equals(c.getIsCertificateDeleted()))
                .map(certificateMapper::toCertificateResponseFromCertificate)
                .toList();

        return CertificateResponseList.builder()
                .certificates(certificateResponses)
                .build();
    }

    @Override
    public CertificateResponse getCertificateById(Long certificateId) {

        Certificate certificate = certificateRepository.findByCertificateIdAndUserEmail(
                certificateId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("certificate", certificateId));

        if (certificate.getIsCertificateDeleted()) {
            throw new ResourceNotFoundException("certificate", certificateId);
        }

        return certificateMapper.toCertificateResponseFromCertificate(certificate);

    }

    @Override
    public CertificateResponse updateCertificate(Long certificateId, CertificateRequest certificateRequest) {

        Certificate certificate = certificateRepository.findByCertificateIdAndUserEmail(
                certificateId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("certificate", certificateId));

        if (certificate.getIsCertificateDeleted()) {
            throw new ResourceNotFoundException("certificate", certificateId);
        }

        if (certificateRequest.certificateTitle() != null && !certificateRequest.certificateTitle().equals(certificate.getCertificateTitle())) {
            certificate.setCertificateTitle(certificateRequest.certificateTitle());
        }
        if (certificateRequest.certificateUrl() != null && !certificateRequest.certificateUrl().equals(certificate.getCertificateUrl())) {
            certificate.setCertificateUrl(certificateRequest.certificateUrl());
        }
        certificate.setIsCertificateDeleted(false);
        certificate.setUserEmail(loggedInUser.getCurrentLoggedInUser());

        return certificateMapper.toCertificateResponseFromCertificate(certificateRepository.save(certificate));

    }

    @Override
    public String deleteCertificate(Long certificateId) {

        Certificate certificate = certificateRepository.findByCertificateIdAndUserEmail(
                certificateId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("certificate", certificateId));

        if (certificate.getIsCertificateDeleted()) {
            return "Certificate with certificateId: " + certificateId + " is already deleted Before.";
        }

        certificate.setIsCertificateDeleted(true);
        certificateRepository.save(certificate);

        return "Certificate with certificateId: " + certificateId + " deleted successfully.";

    }

}
