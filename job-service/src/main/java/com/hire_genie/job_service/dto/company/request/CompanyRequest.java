package com.hire_genie.job_service.dto.company.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CompanyRequest(

        @NotBlank
        @Size(min = 1, max = 150, message = "Company Name should be between 1 to 150 characters")
        String companyName,

        @NotBlank
        @Size(min = 1, max = 150, message = "Company Url should be between 1 to 150 characters")
        String companyUrl

) {
}
