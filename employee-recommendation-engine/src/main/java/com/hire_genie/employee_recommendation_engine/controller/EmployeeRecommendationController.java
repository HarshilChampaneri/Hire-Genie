package com.hire_genie.employee_recommendation_engine.controller;

import com.hire_genie.employee_recommendation_engine.dtoMappings.EmployeeProfile;
import com.hire_genie.employee_recommendation_engine.security.util.LoggedInUser;
import com.hire_genie.employee_recommendation_engine.service.EmployeeRecommendationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees-recommendation")
public class EmployeeRecommendationController {

    private final EmployeeRecommendationService recommendationService;
    private final LoggedInUser loggedInUser;

    @PostMapping("/recommend-employees/{jobId}")
    public ResponseEntity<List<EmployeeProfile>> recommendEmployee(
            HttpServletRequest request,
            @PathVariable Long jobId
    ) throws Exception {

        if (!loggedInUser.isRecruiter()) {
            throw new Exception("You are Unauthorized to use this feature");
        }

        String secret = request.getHeader("X-Internal-Secret");
        String email = request.getHeader("X-User-Email");
        String roles = request.getHeader("X-User-Roles");

        return ResponseEntity.ok(recommendationService.recommendEmployee(secret, email, roles, jobId));
    }

}
