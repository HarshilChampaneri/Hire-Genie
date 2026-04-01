package com.hire_genie.job_service.controller;

import com.hire_genie.job_service.dto.company.request.CompanyRequest;
import com.hire_genie.job_service.dto.company.response.CompanyPageResponse;
import com.hire_genie.job_service.dto.company.response.CompanyResponse;
import com.hire_genie.job_service.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/companies")
    public ResponseEntity<CompanyResponse> addNewCompany(@RequestBody @Valid CompanyRequest companyRequest) {

        return ResponseEntity.ok(companyService.addNewCompany(companyRequest));

    }

    @GetMapping("/companies")
    public ResponseEntity<CompanyPageResponse> getAllCompanies(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "companyName", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {

        return ResponseEntity.ok(companyService.getAllCompanies(page, size, sortBy, sortDir));

    }

    @PutMapping("/companies/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompanyById(
            @PathVariable("companyId") Long companyId,
            @RequestBody @Valid CompanyRequest companyRequest
    ) {

        return ResponseEntity.ok(companyService.updateCompany(companyId, companyRequest));

    }

    @DeleteMapping("/companies/{companyId}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable Long companyId) {

        return ResponseEntity.ok(companyService.deleteCompanyById(companyId));

    }

}
