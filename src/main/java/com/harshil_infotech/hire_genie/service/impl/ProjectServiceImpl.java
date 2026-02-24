package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequest;
import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequestList;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponse;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponseList;
import com.harshil_infotech.hire_genie.exception.InvalidProjectDateRangeException;
import com.harshil_infotech.hire_genie.exception.ProjectEndDateRequiredException;
import com.harshil_infotech.hire_genie.exception.ResourceNotFoundException;
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
            project.setIsProjectDeleted(false);
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
                .filter(project -> Boolean.FALSE.equals(project.getIsProjectDeleted()))
                .map(projectMapper::toProjectResponseFromProject)
                .toList();

        return ProjectResponseList.builder()
                .projects(projectResponseList)
                .build();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest projectRequest) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project", projectId));
        log.info("Project with projectId : " + projectId + " found successfully.");

        Project mappedProject = projectMapper.toProjectFromProjectRequest(projectRequest);
        log.info("Mapping of dto to entity done successfully.");

        if (mappedProject.getIsProjectInProgress() != null && !project.getIsProjectInProgress().equals(mappedProject.getIsProjectInProgress())) {
            project.setIsProjectInProgress(mappedProject.getIsProjectInProgress());
        }
        if (mappedProject.getProjectDescription() != null && !project.getProjectDescription().equals(mappedProject.getProjectDescription())) {
            project.setProjectDescription(mappedProject.getProjectDescription());
        }
        if (mappedProject.getProjectName() != null && !project.getProjectName().equals(mappedProject.getProjectName())) {
            project.setProjectName(mappedProject.getProjectName());
        }
        if (mappedProject.getProjectUrl() != null && !project.getProjectUrl().equals(mappedProject.getProjectUrl())) {
            project.setProjectUrl(mappedProject.getProjectUrl());
        }
        if (mappedProject.getProjectEndDate() != null && !project.getProjectEndDate().equals(mappedProject.getProjectEndDate())) {
            project.setProjectEndDate(mappedProject.getProjectEndDate());
        }
        if (mappedProject.getProjectStartDate() != null && !project.getProjectStartDate().equals(mappedProject.getProjectStartDate())) {
            project.setProjectStartDate(mappedProject.getProjectStartDate());
        }
        if (mappedProject.getProjectTechStacks() != null && !project.getProjectTechStacks().equals(mappedProject.getProjectTechStacks())) {
            project.setProjectTechStacks(mappedProject.getProjectTechStacks());
        }
        project.setIsProjectDeleted(false);
        log.info("Values updated successfully in the project.");

        project = projectRepository.save(project);
        log.info("Updated project saved successfully. Returning the Response.");

        return projectMapper.toProjectResponseFromProject(project);
    }

    @Override
    public String deleteProject(Long projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project", projectId));

        if (project.getIsProjectDeleted()) {
            return "Project with projectId: " + projectId + " is already deleted Before.";
        }

        project.setIsProjectDeleted(true);
        projectRepository.save(project);

        return "Project with projectId : " + projectId + " deleted successfully.";
    }
}
