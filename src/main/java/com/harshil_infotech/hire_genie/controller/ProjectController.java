package com.harshil_infotech.hire_genie.controller;

import com.harshil_infotech.hire_genie.dto.request.ProjectRequestList;
import com.harshil_infotech.hire_genie.dto.response.ProjectResponse;
import com.harshil_infotech.hire_genie.dto.response.ProjectResponseList;
import com.harshil_infotech.hire_genie.repository.ProjectRepository;
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
    public ResponseEntity<List<ProjectResponse>> addProjects(@RequestBody @Valid ProjectRequestList projectRequestList) {
        return ResponseEntity.ok(projectService.addProjects(projectRequestList));
    }

    @GetMapping("/projects")
    public ResponseEntity<ProjectResponseList> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

}
