package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.dto.certificate.request.CertificateRequest;
import com.harshil_infotech.hire_genie.dto.certificate.request.CertificateRequestList;
import com.harshil_infotech.hire_genie.dto.certificate.response.CertificateResponse;
import com.harshil_infotech.hire_genie.dto.certificate.response.CertificateResponseList;
import com.harshil_infotech.hire_genie.exception.ResourceNotFoundException;
import com.harshil_infotech.hire_genie.mapper.CertificateMapper;
import com.harshil_infotech.hire_genie.model.Certificate;
import com.harshil_infotech.hire_genie.repository.CertificateRepository;
import com.harshil_infotech.hire_genie.service.CertificateService;
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

    @Override
    public List<CertificateResponse> addCertificates(
            CertificateRequestList certificateRequestList
    ) {

        List<Certificate> certificates = certificateRequestList
                .certificateRequests()
                .stream()
                .map(certificateMapper::toCertificateFromCertificateRequest)
                .toList();

        for (Certificate certificate : certificates) {
            certificate.setIsCertificateDeleted(false);
        }

        return certificateRepository.saveAll(certificates)
                .stream()
                .map(certificateMapper::toCertificateResponseFromCertificate)
                .toList();

    }

    @Override
    public CertificateResponseList getAllCertificates() throws Exception {

        List<Certificate> certificates = certificateRepository.findAll();

        if (certificates.isEmpty()) {
            throw new Exception("No certificates Found");
        }

        List<CertificateResponse> certificateResponses = certificates
                .stream()
                .filter(c -> Boolean.FALSE.equals(c.getIsCertificateDeleted()))
                .map(certificateMapper::toCertificateResponseFromCertificate)
                .toList();

        return CertificateResponseList.builder()
                .certificates(certificateResponses)
                .build();
    }

    @Override
    public CertificateResponse getCertificateById(Long certificateId) {

        Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(() ->
                new ResourceNotFoundException("certificate", certificateId));

        if (certificate.getIsCertificateDeleted()) {
            throw new ResourceNotFoundException("certificate", certificateId);
        }

        return certificateMapper.toCertificateResponseFromCertificate(certificate);

    }

    @Override
    public CertificateResponse updateCertificate(Long certificateId, CertificateRequest certificateRequest) {

        Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(() ->
                new ResourceNotFoundException("certificate", certificateId));

        if (certificate.getIsCertificateDeleted()) {
            throw new ResourceNotFoundException("certificate", certificateId);
        }

        if (certificateRequest.certificateTitle() != null && !certificateRequest.certificateTitle().equals(certificate.getCertificateTitle())) {
            certificate.setCertificateTitle(certificateRequest.certificateTitle());
        }
        if (certificateRequest.certificateUrl() != null && !certificateRequest.certificateUrl().equals(certificate.getCertificateUrl())) {
            certificate.setCertificateUrl(certificateRequest.certificateUrl());
        }

        return certificateMapper.toCertificateResponseFromCertificate(certificateRepository.save(certificate));

    }

    @Override
    public String deleteCertificate(Long certificateId) {

        Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(() ->
                new ResourceNotFoundException("certificate", certificateId));

        if (certificate.getIsCertificateDeleted()) {
            return "Certificate with certificateId: " + certificateId + " is already deleted Before.";
        }

        certificate.setIsCertificateDeleted(true);
        certificateRepository.save(certificate);

        return "Certificate with certificateId: " + certificateId + " deleted successfully.";

    }

}
