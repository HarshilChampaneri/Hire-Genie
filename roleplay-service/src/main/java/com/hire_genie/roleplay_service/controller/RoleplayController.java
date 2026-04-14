package com.hire_genie.roleplay_service.controller;

import com.hire_genie.roleplay_service.dto.roleplay.RoleplayDTO;
import com.hire_genie.roleplay_service.service.RoleplayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roleplay")
public class RoleplayController {

    private final RoleplayService roleplayService;

    @GetMapping("/start-roleplay/{jobId}")
    public ResponseEntity<RoleplayDTO> startRoleplay(@PathVariable Long jobId, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(roleplayService.startRoleplayService(jobId, request));
    }

}
