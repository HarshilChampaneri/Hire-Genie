package com.hire_genie.resume_builder.dto.project.request;

import jakarta.validation.Valid;

import java.util.List;

public record ProjectRequestList(

        @Valid
        List<ProjectRequest> projectRequests

) {
}
