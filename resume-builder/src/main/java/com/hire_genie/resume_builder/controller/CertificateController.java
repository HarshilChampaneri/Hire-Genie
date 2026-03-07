package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.certificate.request.CertificateRequest;
import com.hire_genie.resume_builder.dto.certificate.request.CertificateRequestList;
import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponse;
import com.hire_genie.resume_builder.dto.certificate.response.CertificateResponseList;
import com.hire_genie.resume_builder.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/certificates")
    public ResponseEntity<List<CertificateResponse>> addCertificates(
            @RequestBody @Valid CertificateRequestList certificateRequestList
    ) {
        return ResponseEntity.ok(certificateService.addCertificates(certificateRequestList));
    }

    @GetMapping("/certificates")
    public ResponseEntity<CertificateResponseList> getAllCertificates() throws Exception {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    @GetMapping("/certificates/{certificateId}")
    public ResponseEntity<CertificateResponse> getCertificateById(
            @PathVariable Long certificateId
    ) {
        return ResponseEntity.ok(certificateService.getCertificateById(certificateId));
    }

    @PutMapping("/certificates/{certificateId}")
    public ResponseEntity<CertificateResponse> updateCertificate(
            @PathVariable Long certificateId,
            @RequestBody @Valid CertificateRequest certificateRequest
    ) {
        return ResponseEntity.ok(certificateService.updateCertificate(certificateId, certificateRequest));
    }

    @DeleteMapping("/certificates/{certificateId}")
    public ResponseEntity<String> deleteCertificate(
            @PathVariable Long certificateId
    ) {
        return ResponseEntity.ok(certificateService.deleteCertificate(certificateId));
    }

}
