package com.hire_genie.job_service.dto.job.response;

import lombok.Builder;

import java.util.List;

@Builder
public record JobPageResponse(
        List<JobResponse> jobResponse,
        Boolean isLastPage,
        Integer totalPages,
        Integer totalElements,
        Integer pageSize,
        Integer pageIndex,
        String sortDir,
        String sortBy
) {
}
