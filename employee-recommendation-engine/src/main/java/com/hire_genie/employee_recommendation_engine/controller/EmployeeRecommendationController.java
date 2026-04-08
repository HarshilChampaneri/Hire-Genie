package com.hire_genie.employee_recommendation_engine.controller;

import com.hire_genie.employee_recommendation_engine.dtoMappings.EmployeeProfile;
import com.hire_genie.employee_recommendation_engine.service.EmployeeRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees-recommendation")
public class EmployeeRecommendationController {

    private final EmployeeRecommendationService recommendationService;

    @PostMapping("/recommend-employees")
    public ResponseEntity<List<EmployeeProfile>> recommendEmployee(
            @RequestBody String jobDescription
    ) {
        return ResponseEntity.ok(recommendationService.recommendEmployee(jobDescription));
    }

}
