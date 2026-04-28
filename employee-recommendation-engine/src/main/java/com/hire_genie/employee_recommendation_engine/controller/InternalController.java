package com.hire_genie.employee_recommendation_engine.controller;

import com.hire_genie.employee_recommendation_engine.dtoMappings.ProfileResponse;
import com.hire_genie.employee_recommendation_engine.dtoMappings.ResumeRequest;
import com.hire_genie.employee_recommendation_engine.service.EmployeeRecommendationService;
import com.hire_genie.employee_recommendation_engine.service.ResumeVectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {

    private final ResumeVectorStoreService resumeVectorStoreService;
    private final EmployeeRecommendationService employeeRecommendationService;

    @PostMapping("resume/store")
    public ResponseEntity<Void> storeResume(@RequestBody ResumeRequest resumeRequest) {
        resumeVectorStoreService.upsertResume(resumeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recommend/employees")
    public ResponseEntity<List<ProfileResponse>> recommendEmployees(@RequestBody String jobDescription) {
        return ResponseEntity.ok(employeeRecommendationService.recommendEmployee(jobDescription));
    }

}
