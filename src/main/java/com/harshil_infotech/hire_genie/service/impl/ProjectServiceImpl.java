package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequest;
import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequestList;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponse;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponseList;
import com.harshil_infotech.hire_genie.exception.InvalidProjectDateRangeException;
import com.harshil_infotech.hire_genie.exception.ProjectEndDateRequiredException;
import com.harshil_infotech.hire_genie.mapper.ProjectMapper;
import com.harshil_infotech.hire_genie.model.Project;
import com.harshil_infotech.hire_genie.repository.ProjectRepository;
import com.harshil_infotech.hire_genie.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public List<ProjectResponse> addProjects(ProjectRequestList projectRequestList) {

        List<Project> projects = projectRequestList
                .projectRequests()
                .stream()
                .map(projectMapper::toProjectFromProjectRequest)
                .toList();

        for (Project project : projects) {
            if (Boolean.FALSE.equals(project.getIsProjectInProgress())) {
                if (project.getProjectEndDate() == null) {
                    throw new ProjectEndDateRequiredException();
                }
                if (project.getProjectEndDate().isBefore(project.getProjectStartDate())) {
                    throw new InvalidProjectDateRangeException();
                }
            }
        }

        return projectRepository.saveAll(projects)
                .stream()
                .map(projectMapper::toProjectResponseFromProject)
                .toList();
    }

    @Override
    public ProjectResponseList getAllProjects() {

        List<Project> projects = projectRepository.findAll();

        List<ProjectResponse> projectResponseList = projects.stream()
                .filter(project -> Boolean.TRUE.equals(project.getIsProjectDeleted()))
                .map(projectMapper::toProjectResponseFromProject)
                .toList();

        return ProjectResponseList.builder()
                .projects(projectResponseList)
                .build();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest projectRequest) {

//        TODO: Need to handle the custom exception here!
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new RuntimeException("Project with projectId : " + projectId + " not found."));

        Project mappedProject = projectMapper.toProjectFromProjectRequest(projectRequest);

        if (!project.getIsProjectInProgress().equals(mappedProject.getIsProjectInProgress())) {
            project.setIsProjectInProgress(mappedProject.getIsProjectInProgress());
        }
        if (!project.getProjectDescription().equals(mappedProject.getProjectDescription())) {
            project.setProjectDescription(mappedProject.getProjectDescription());
        }
        if (!project.getProjectName().equals(mappedProject.getProjectName())) {
            project.setProjectName(mappedProject.getProjectName());
        }
        if (!project.getProjectUrl().equals(mappedProject.getProjectUrl())) {
            project.setProjectUrl(mappedProject.getProjectUrl());
        }
        if (!project.getProjectEndDate().equals(mappedProject.getProjectEndDate())) {
            project.setProjectEndDate(mappedProject.getProjectEndDate());
        }
        if (!project.getProjectStartDate().equals(mappedProject.getProjectStartDate())) {
            project.setProjectStartDate(mappedProject.getProjectStartDate());
        }
        if (!project.getProjectTechStacks().equals(mappedProject.getProjectTechStacks())) {
            project.setProjectTechStacks(mappedProject.getProjectTechStacks());
        }

        project = projectRepository.save(project);

        return projectMapper.toProjectResponseFromProject(project);
    }

    @Override
    public String deleteProject(Long projectId) {

//        TODO: Need to handle the custom exception here!
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new RuntimeException("Project with projectId: " + projectId + " not found."));

        project.setIsProjectDeleted(true);
        projectRepository.save(project);

        return "Project with projectId : " + projectId + " deleted successfully.";
    }
}
