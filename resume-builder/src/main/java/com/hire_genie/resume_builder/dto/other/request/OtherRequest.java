package com.hire_genie.resume_builder.dto.other.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record OtherRequest(

        @NotEmpty
        List<String> description

) {
}
