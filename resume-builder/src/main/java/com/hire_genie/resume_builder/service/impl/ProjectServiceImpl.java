package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.project.request.ProjectRequest;
import com.hire_genie.resume_builder.dto.project.request.ProjectRequestList;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponseList;
import com.hire_genie.resume_builder.exception.EndDateRequiredException;
import com.hire_genie.resume_builder.exception.InvalidDateRangeException;
import com.hire_genie.resume_builder.exception.ResourceNotFoundException;
import com.hire_genie.resume_builder.mapper.ProjectMapper;
import com.hire_genie.resume_builder.model.Project;
import com.hire_genie.resume_builder.repository.ProjectRepository;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hire_genie.resume_builder.util.StaticConstants.ALL_PROJECTS;
import static com.hire_genie.resume_builder.util.StaticConstants.PROJECTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final LoggedInUser loggedInUser;

    @Override
    @CacheEvict(value = ALL_PROJECTS, key = "@loggedInUser.getCurrentLoggedInUser()")
    public List<ProjectResponse> addProjects(ProjectRequestList projectRequestList) {

        List<Project> projects = projectRequestList
                .projectRequests()
                .stream()
                .map(projectMapper::toProjectFromProjectRequest)
                .toList();

        for (Project project : projects) {
            if (Boolean.FALSE.equals(project.getIsProjectInProgress())) {
                if (project.getProjectEndDate() == null) {
                    throw new EndDateRequiredException("project");
                }
                if (project.getProjectEndDate().isBefore(project.getProjectStartDate())) {
                    throw new InvalidDateRangeException("project");
                }
            }
            project.setIsProjectDeleted(false);
            project.setUserEmail(loggedInUser.getCurrentLoggedInUser());
        }

        return projectRepository.saveAll(projects)
                .stream()
                .map(projectMapper::toProjectResponseFromProject)
                .toList();
    }

    @Override
    @Cacheable(value = ALL_PROJECTS, key = "@loggedInUser.getCurrentLoggedInUser()")
    public ProjectResponseList getAllProjects() throws Exception {

        List<Project> projects = projectRepository.findActiveProjects(loggedInUser.getCurrentLoggedInUser());

        if (projects.isEmpty()) {
            throw new Exception("No Projects Found");
        }

        List<ProjectResponse> projectResponseList = projects.stream()
//                .filter(project -> Boolean.FALSE.equals(project.getIsProjectDeleted()))
                .map(projectMapper::toProjectResponseFromProject)
                .toList();

        return ProjectResponseList.builder()
                .projects(projectResponseList)
                .build();
    }

    @Override
    @Cacheable(
            value = PROJECTS,
            key = "#projectId + '_' + @loggedInUser.getCurrentLoggedInUser()"
    )
    public ProjectResponse getProjectById(Long projectId) {

        Project project = projectRepository.findByProjectIdAndUserEmailAndIsProjectDeletedFalse(
                projectId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        if (project.getIsProjectDeleted()) {
            throw new ResourceNotFoundException("Project", projectId);
        }

        return projectMapper.toProjectResponseFromProject(project);
    }

    @Override
    @Caching(
            put = @CachePut(
                    value = PROJECTS,
                    key = "#projectId + '_' + @loggedInUser.getCurrentLoggedInUser()"
            ),
            evict = @CacheEvict(
                    value = ALL_PROJECTS,
                    key = "@loggedInUser.getCurrentLoggedInUser()"
            )
    )
    public ProjectResponse updateProject(Long projectId, ProjectRequest projectRequest) {

        Project project = projectRepository.findByProjectIdAndUserEmailAndIsProjectDeletedFalse(
                projectId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
        log.info("Project with projectId : " + projectId + " found successfully.");

        if (project.getIsProjectDeleted()) {
            throw new ResourceNotFoundException("Project", projectId);
        }

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
        project.setUserEmail(loggedInUser.getCurrentLoggedInUser());
        log.info("Values updated successfully in the project.");

        project = projectRepository.save(project);
        log.info("Updated project saved successfully. Returning the Response.");

        return projectMapper.toProjectResponseFromProject(project);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(
                            value = PROJECTS,
                            key = "#projectId + '_' + @loggedInUser.getCurrentLoggedInUser()"
                    ),
                    @CacheEvict(
                            value = ALL_PROJECTS,
                            key = "@loggedInUser.getCurrentLoggedInUser()"
                    )
            }
    )
    public String deleteProject(Long projectId) {

        Project project = projectRepository.findByProjectIdAndUserEmailAndIsProjectDeletedFalse(
                projectId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        if (project.getIsProjectDeleted()) {
            return "Project with projectId: " + projectId + " is already deleted Before.";
        }

        project.setIsProjectDeleted(true);
        projectRepository.save(project);

        return "Project with projectId : " + projectId + " deleted successfully.";
    }
}
