package com.hire_genie.resume_builder.dto.experience.request;

import jakarta.validation.Valid;
import lombok.Builder;

import java.util.List;

@Builder
public record ExperienceRequestList(

        @Valid
        List<ExperienceRequest> experienceRequests

) {
}
