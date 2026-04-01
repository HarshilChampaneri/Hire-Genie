package com.hire_genie.job_service.dto.company.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CompanyPageResponse(
        List<CompanyResponse> companyResponse,
        Boolean isLastPage,
        Integer totalPages,
        Integer totalElements,
        Integer pageSize,
        Integer pageIndex,
        String sortDir,
        String sortBy
) {
}
