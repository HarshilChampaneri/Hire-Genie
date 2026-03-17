package com.hire_genie.job_service.controller;

import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import com.hire_genie.job_service.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @PostMapping("/{companyId}")
    public ResponseEntity<JobResponse> addNewJob(
            @PathVariable Long companyId,
            @RequestBody @Valid JobRequest jobRequest
    ) {
        return ResponseEntity.ok(jobService.addNewJobByCompanyId(companyId, jobRequest));
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<List<JobResponse>> getAllJobsByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobService.getAllJobsByCompanyId(companyId));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJobByJobId(
            @PathVariable Long jobId,
            @RequestBody @Valid JobRequest jobRequest
    ) {
        return ResponseEntity.ok(jobService.updateJobByJobId(jobId, jobRequest));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> deleteJobById(
            @PathVariable Long jobId
    ) {
        return ResponseEntity.ok(jobService.deleteJobById(jobId));
    }

}
