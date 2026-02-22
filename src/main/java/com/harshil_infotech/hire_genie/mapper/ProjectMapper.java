package com.harshil_infotech.hire_genie.mapper;

import com.harshil_infotech.hire_genie.dto.request.ProjectRequest;
import com.harshil_infotech.hire_genie.dto.response.ProjectResponse;
import com.harshil_infotech.hire_genie.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toProjectFromProjectRequestList(ProjectRequest projectRequest) {

        return Project.builder()
                .projectName(projectRequest.projectName())
                .projectUrl(projectRequest.projectUrl())
                .projectTechStacks(projectRequest.projectTechStacks())
                .projectStartDate(projectRequest.projectStartDate())
                .isProjectInProgress(projectRequest.isProjectInProgress())
                .projectEndDate(projectRequest.projectEndDate())
                .projectDescription(projectRequest.projectDescription())
                .build();
    }

    public ProjectResponse toProjectResponseFromProject(Project project) {

        return ProjectResponse.builder()
                .projectId(project.getProjectId())
                .projectStartDate(project.getProjectStartDate())
                .projectTechStacks(project.getProjectTechStacks())
                .projectEndDate(project.getProjectEndDate())
                .isProjectInProgress(project.getIsProjectInProgress())
                .projectDescription(project.getProjectDescription())
                .projectName(project.getProjectName())
                .projectUrl(project.getProjectUrl())
                .build();
    }

}
