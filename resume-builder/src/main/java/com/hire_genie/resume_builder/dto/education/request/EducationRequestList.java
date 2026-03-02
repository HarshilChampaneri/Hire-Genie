package com.hire_genie.resume_builder.dto.education.request;

import lombok.Builder;

import java.util.List;

@Builder
public record EducationRequestList(
        List<EducationRequest> educationRequests
) {
}
