package com.harshil_infotech.hire_genie.mapper;

import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequest;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponse;
import com.harshil_infotech.hire_genie.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toProjectFromProjectRequest(ProjectRequest projectRequest) {

        boolean inProgress = Boolean.TRUE.equals(projectRequest.isProjectInProgress());

        return Project.builder()
                .projectName(projectRequest.projectName())
                .projectUrl(projectRequest.projectUrl())
                .projectTechStacks(projectRequest.projectTechStacks())
                .projectStartDate(projectRequest.projectStartDate())

                //user -> code-understanding : null -> false, false -> false, true -> true
                .isProjectInProgress(inProgress)
                .projectEndDate(Boolean.TRUE.equals(projectRequest.isProjectInProgress())
                        ? null
                        : projectRequest.projectEndDate()
                )
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
