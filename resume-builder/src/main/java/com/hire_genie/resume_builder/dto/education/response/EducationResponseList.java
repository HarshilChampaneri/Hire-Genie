package com.hire_genie.resume_builder.dto.education.response;

import lombok.Builder;

import java.util.List;

@Builder
public record EducationResponseList(
        List<EducationResponse> educations
) {
}
