package com.harshil_infotech.hire_genie.dto.education.request;

import lombok.Builder;

import java.util.List;

@Builder
public record EducationRequestList(
        List<EducationRequest> educationRequests
) {
}
