package com.hire_genie.roleplay_service.controller;

import com.hire_genie.roleplay_service.dto.roleplay.RoleplayDTO;
import com.hire_genie.roleplay_service.service.RoleplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final RoleplayService roleplayService;

    @PostMapping("/start/roleplay")
    public ResponseEntity<RoleplayDTO> startRoleplay(@RequestBody String jobDescription) throws Exception {
        return ResponseEntity.ok(roleplayService.startRoleplayService(jobDescription));
    }

}
