package com.harshil_infotech.hire_genie.dto.other.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OtherResponse(
        Long otherId,
        List<String> description
) {
}
