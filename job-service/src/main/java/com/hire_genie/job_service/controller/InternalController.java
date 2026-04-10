package com.hire_genie.job_service.controller;

import com.hire_genie.job_service.service.impl.InternalJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final InternalJobService internalJobService;

    @GetMapping("/fetch/job-description/{jobId}")
    public ResponseEntity<String> fetchJobDescription(@PathVariable Long jobId) {
        return ResponseEntity.ok(internalJobService.fetchJobDescription(jobId));
    }

}
