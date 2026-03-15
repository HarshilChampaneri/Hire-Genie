package com.hire_genie.job_service.service.impl;

import com.hire_genie.job_service.dto.company.request.CompanyRequest;
import com.hire_genie.job_service.dto.company.response.CompanyResponse;
import com.hire_genie.job_service.mapper.CompanyMapper;
import com.hire_genie.job_service.model.Company;
import com.hire_genie.job_service.repository.CompanyRepository;
import com.hire_genie.job_service.security.util.LoggedInUser;
import com.hire_genie.job_service.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final LoggedInUser loggedInUser;

    public CompanyResponse addNewCompany(CompanyRequest companyRequest) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Company company = companyMapper.toCompanyFromCompanyRequest(companyRequest);
        company.setIsCompanyDeleted(false);
        company.setUserEmail(userEmail);

        return companyMapper.toCompanyResponseFromCompany(companyRepository.save(company));
    }

//    public List<CompanyResponse> getAllCompanies() {
//
//        List<Company> companies = companyRepository.findAllActiveCompanies();
//        if (companies.isEmpty()) {
//
//        }
//
//    }

}
