package com.hire_genie.job_service.service;

import com.hire_genie.job_service.dto.company.request.CompanyRequest;
import com.hire_genie.job_service.dto.company.response.CompanyPageResponse;
import com.hire_genie.job_service.dto.company.response.CompanyResponse;

public interface CompanyService {
    CompanyResponse addNewCompany(CompanyRequest companyRequest);

    CompanyPageResponse getAllCompanies(int page, int size, String sortBy, String sortDir);

    CompanyResponse updateCompany(Long companyId, CompanyRequest companyRequest);

    String deleteCompanyById(Long companyId);
}
