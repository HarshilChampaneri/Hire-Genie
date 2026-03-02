package com.hire_genie.resume_builder.dto.other.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OtherResponse(
        Long otherId,
        List<String> description
) {
}
