package com.harshil_infotech.hire_genie.dto.education.response;

import lombok.Builder;

import java.util.List;

@Builder
public record EducationResponseList(
        List<EducationResponse> educations
) {
}
