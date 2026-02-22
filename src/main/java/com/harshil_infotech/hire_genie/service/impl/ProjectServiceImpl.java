package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.dto.request.ProjectRequestList;
import com.harshil_infotech.hire_genie.dto.response.ProjectResponse;
import com.harshil_infotech.hire_genie.dto.response.ProjectResponseList;
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
                .map(projectMapper::toProjectFromProjectRequestList)
                .toList();

        projects = projectRepository.saveAll(projects);

        return projects
                .stream()
                .map(projectMapper::toProjectResponseFromProject)
                .toList();
    }

    @Override
    public ProjectResponseList getAllProjects() {

        List<Project> projects = projectRepository.findAll();

        List<ProjectResponse> projectResponseList = projects.stream()
                .map(projectMapper::toProjectResponseFromProject)
                .toList();

        return ProjectResponseList.builder()
                .projects(projectResponseList)
                .build();
    }

}
