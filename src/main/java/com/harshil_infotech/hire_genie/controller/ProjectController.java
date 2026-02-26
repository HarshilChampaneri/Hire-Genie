package com.harshil_infotech.hire_genie.controller;

import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequest;
import com.harshil_infotech.hire_genie.dto.project.request.ProjectRequestList;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponse;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectResponseList;
import com.harshil_infotech.hire_genie.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<List<ProjectResponse>> addProjects(
            @RequestBody @Valid ProjectRequestList projectRequestList
    ) {
        return ResponseEntity.ok(projectService.addProjects(projectRequestList));
    }

    @GetMapping("/projects")
    public ResponseEntity<ProjectResponseList> getAllProjects() {
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
