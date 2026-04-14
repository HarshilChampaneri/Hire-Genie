package com.hire_genie.roleplay_service.service;

import com.hire_genie.roleplay_service.dto.roleplay.RoleplayDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface RoleplayService {

    RoleplayDTO startRoleplayService(Long jobId, HttpServletRequest request) throws Exception;

}
