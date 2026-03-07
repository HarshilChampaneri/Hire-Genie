package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.project.request.ProjectRequest;
import com.hire_genie.resume_builder.dto.project.request.ProjectRequestList;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectResponseList;
import com.hire_genie.resume_builder.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<List<ProjectResponse>> addProjects(
            @RequestBody @Valid ProjectRequestList projectRequestList
    ) {
        return ResponseEntity.ok(projectService.addProjects(projectRequestList));
    }

    @GetMapping("/projects")
    public ResponseEntity<ProjectResponseList> getAllProjects() throws Exception {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectRequest projectRequest
    ) {
        return ResponseEntity.ok(projectService.updateProject(projectId, projectRequest));
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<String> deleteProject(
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(projectService.deleteProject(projectId));
    }

}
