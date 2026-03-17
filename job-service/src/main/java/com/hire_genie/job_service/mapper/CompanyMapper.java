package com.hire_genie.job_service.mapper;

import com.hire_genie.job_service.dto.company.request.CompanyRequest;
import com.hire_genie.job_service.dto.company.response.CompanyResponse;
import com.hire_genie.job_service.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toCompanyFromCompanyRequest(CompanyRequest companyRequest) {

        return Company.builder()
                .companyUrl(companyRequest.companyUrl())
                .companyName(companyRequest.companyName())
                .build();

    }

    public CompanyResponse toCompanyResponseFromCompany(Company company) {

        return CompanyResponse.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyUrl(company.getCompanyUrl())
                .build();

    }

}
