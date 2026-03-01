package com.harshil_infotech.hire_genie.dto.other.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OtherDescriptionResponse(
        List<String> otherDescription
) {
}
