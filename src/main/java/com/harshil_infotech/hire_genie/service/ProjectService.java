package com.harshil_infotech.hire_genie.service;

import com.harshil_infotech.hire_genie.dto.request.ProjectRequestList;
import com.harshil_infotech.hire_genie.dto.response.ProjectResponse;
import com.harshil_infotech.hire_genie.dto.response.ProjectResponseList;
import jakarta.validation.Valid;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> addProjects(@Valid ProjectRequestList projectRequestList);

    ProjectResponseList getAllProjects();
}
