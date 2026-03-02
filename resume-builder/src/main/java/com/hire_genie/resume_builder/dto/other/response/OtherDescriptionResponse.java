package com.hire_genie.resume_builder.dto.other.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OtherDescriptionResponse(
        List<String> otherDescription
) {
}
