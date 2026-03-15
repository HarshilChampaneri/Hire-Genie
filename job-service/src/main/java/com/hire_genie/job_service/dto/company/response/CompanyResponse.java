package com.hire_genie.job_service.dto.company.response;

import lombok.Builder;

@Builder
public record CompanyResponse(
        Long companyId,
        String companyName,
        String companyUrl
) {
}
