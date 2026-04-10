package com.hire_genie.job_service.controller;

import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobPageResponse;
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
    public ResponseEntity<JobPageResponse> getAllJobs(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "jobType", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return ResponseEntity.ok(jobService.getAllJobs(page, size, sortBy, sortDir));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<JobPageResponse> getAllJobsByCompanyId(
            @PathVariable Long companyId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "jobName", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return ResponseEntity.ok(jobService.getAllJobsByCompanyId(companyId, page, size, sortBy, sortDir));
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
