package com.harshil_infotech.hire_genie.service;

import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequest;
import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequestList;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponse;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponseList;
import jakarta.validation.Valid;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> addProjects(@Valid ProjectRequestList projectRequestList);

    ProjectResponseList getAllProjects();

    ProjectResponse getProjectById(Long projectId);

    ProjectResponse updateProject(Long projectId, @Valid ProjectRequest projectRequest);

    String deleteProject(Long projectId);
}
