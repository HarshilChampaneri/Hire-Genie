package com.harshil_infotech.hire_genie.dto.request;

import jakarta.validation.Valid;

import java.util.List;

public record ProjectRequestList(

        @Valid
        List<ProjectRequest> projectRequests

) {
}
