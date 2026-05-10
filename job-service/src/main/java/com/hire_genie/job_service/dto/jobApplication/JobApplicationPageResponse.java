package com.hire_genie.job_service.dto.jobApplication;

import lombok.Builder;

import java.util.List;

@Builder
public record JobApplicationPageResponse(
        List<JobApplicationResponse> jobApplicationResponses,
        Boolean isLastPage,
        Integer totalPages,
        Integer totalElements,
        Integer pageSize,
        Integer pageIndex,
        String sortDir,
        String sortBy
) {
}
