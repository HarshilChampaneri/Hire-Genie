package com.harshil_infotech.hire_genie.dto.experience.request;

import jakarta.validation.Valid;
import lombok.Builder;

import java.util.List;

@Builder
public record ExperienceRequestList(

        @Valid
        List<ExperienceRequest> experienceRequests

) {
}
