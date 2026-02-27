package com.harshil_infotech.hire_genie.dto.experience.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ExperienceDescriptionResponse(
        List<String> experienceDescription
) {
}
