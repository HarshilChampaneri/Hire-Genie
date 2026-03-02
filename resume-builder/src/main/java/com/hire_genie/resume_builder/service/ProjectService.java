package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.project.request.ProjectRequest;
import com.hire_genie.resume_builder.dto.project.request.ProjectRequestList;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponseList;
import jakarta.validation.Valid;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> addProjects(@Valid ProjectRequestList projectRequestList);

    ProjectResponseList getAllProjects() throws Exception;

    ProjectResponse getProjectById(Long projectId);

    ProjectResponse updateProject(Long projectId, @Valid ProjectRequest projectRequest);

    String deleteProject(Long projectId);
}
