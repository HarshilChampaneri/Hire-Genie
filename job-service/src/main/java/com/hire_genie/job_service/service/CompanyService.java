package com.hire_genie.job_service.service;

import com.hire_genie.job_service.dto.company.request.CompanyRequest;
import com.hire_genie.job_service.dto.company.response.CompanyResponse;
import org.springframework.data.domain.Page;

public interface CompanyService {
    CompanyResponse addNewCompany(CompanyRequest companyRequest);

    Page<CompanyResponse> getAllCompanies(int page, int size, String sortBy, String sortDir);

    CompanyResponse updateCompany(Long companyId, CompanyRequest companyRequest);

    String deleteCompanyById(Long companyId);
}
