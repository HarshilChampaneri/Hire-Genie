package com.hire_genie.job_service.service.impl;

import com.hire_genie.job_service.dto.company.request.CompanyRequest;
import com.hire_genie.job_service.dto.company.response.CompanyPageResponse;
import com.hire_genie.job_service.dto.company.response.CompanyResponse;
import com.hire_genie.job_service.exception.InvalidAccessException;
import com.hire_genie.job_service.exception.ResourceNotFoundException;
import com.hire_genie.job_service.mapper.CompanyMapper;
import com.hire_genie.job_service.model.Company;
import com.hire_genie.job_service.model.Job;
import com.hire_genie.job_service.repository.CompanyRepository;
import com.hire_genie.job_service.security.util.LoggedInUser;
import com.hire_genie.job_service.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.hire_genie.job_service.util.StaticConstants.COMPANIES;
import static com.hire_genie.job_service.util.StaticConstants.COMPANIES_PAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final LoggedInUser loggedInUser;

    @Override
    @Caching(
            put = @CachePut(value = COMPANIES, key = "#result.companyId"),
            evict = @CacheEvict(value = COMPANIES_PAGE, allEntries = true)
    )
    public CompanyResponse addNewCompany(CompanyRequest companyRequest) {

        if (!loggedInUser.isRecruiter()) {
            throw new InvalidAccessException("You are not Authorized to perform this action.");
        }

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Company company = companyMapper.toCompanyFromCompanyRequest(companyRequest);
        company.setJobs(new ArrayList<>());
        company.setIsCompanyDeleted(false);
        company.setUserEmail(userEmail);

        return companyMapper.toCompanyResponseFromCompany(companyRepository.save(company));
    }

    @Override
    @Cacheable(
            value = COMPANIES_PAGE,
            key = "#page + '_' + #size + '_' + #sortBy + '_' + #sortDir"
    )
    public CompanyPageResponse getAllCompanies(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Company> companies = companyRepository.findAllActiveCompanies(pageable);
        if (companies.isEmpty()) {
            throw new ResourceNotFoundException("Company");
        }

        return CompanyPageResponse.builder()
                .companyResponse(companies.map(companyMapper::toCompanyResponseFromCompany).getContent())
                .isLastPage(companies.isLast())
                .totalPages(companies.getTotalPages())
                .totalElements((int) companies.getTotalElements())
                .pageSize(companies.getSize())
                .pageIndex(companies.getNumber())
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();

    }

    @Override
    @Caching(
            put = @CachePut(value = COMPANIES, key = "#companyId"),
            evict = @CacheEvict(value = COMPANIES_PAGE, allEntries = true)
    )
    public CompanyResponse updateCompany(Long companyId, CompanyRequest companyRequest) {

        if (!loggedInUser.isRecruiter()) {
            throw new InvalidAccessException("You are not Authorized to perform this action.");
        }

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Company company = companyRepository.findCompanyById(companyId, userEmail);
        if (company == null) {
            throw new ResourceNotFoundException("Company", companyId);
        }

        if (!companyRequest.companyName().isEmpty() && !company.getCompanyName().equals(companyRequest.companyName())) {
            company.setCompanyName(companyRequest.companyName());
        }
        if (!companyRequest.companyUrl().isEmpty() && !company.getCompanyUrl().equals(companyRequest.companyUrl())) {
            company.setCompanyUrl(companyRequest.companyUrl());
        }
        company.setIsCompanyDeleted(false);
        company.setUserEmail(userEmail);

        return companyMapper.toCompanyResponseFromCompany(companyRepository.save(company));
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = COMPANIES, key = "#companyId"),
                    @CacheEvict(value = COMPANIES_PAGE, allEntries = true)
            }
    )
    public String deleteCompanyById(Long companyId) {

        if (!loggedInUser.isRecruiter()) {
            throw new InvalidAccessException("You are not Authorized to perform this action.");
        }

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Company company = companyRepository.findCompanyById(companyId, userEmail);
        if (company == null) {
            throw new ResourceNotFoundException("Company", companyId);
        }

        company.setIsCompanyDeleted(true);
        for (Job job : company.getJobs()) {
            job.setIsJobDeleted(true);
        }

        companyRepository.save(company);

        return "Company with companyId: " + companyId + " deleted successfully.";
    }
}
