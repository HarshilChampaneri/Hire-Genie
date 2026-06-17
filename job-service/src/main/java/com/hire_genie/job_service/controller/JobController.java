package com.hire_genie.job_service.controller;

import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobPageResponse;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import com.hire_genie.job_service.dto.jobApplication.JobApplicationPageResponse;
import com.hire_genie.job_service.dto.jobApplication.JobApplicationRequest;
import com.hire_genie.job_service.dto.roleplay.RoleplayDTO;
import com.hire_genie.job_service.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
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
            @RequestBody @Valid JobRequest jobRequest,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(jobService.addNewJobByCompanyId(companyId, jobRequest, request));
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

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<String> applyForJob(
            @PathVariable Long jobId
    ) {
        jobService.applyForJob(jobId);
        return ResponseEntity.ok("Application submitted successfully. You will receive an email shortly.");
    }

    @PostMapping("/recommend/employees/{jobId}")
    public ResponseEntity<List<ProfileResponse>> recommendEmployees(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.fetchJobDescriptionAndRecommendEmployees(jobId));
    }

    @PostMapping("/start/roleplay/{jobId}")
    public ResponseEntity<RoleplayDTO> startRoleplay(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.startRoleplay(jobId));
    }

    @PostMapping("/accept/applicant/{jobApplicationId}")
    public ResponseEntity<String> acceptJobApplicationAndCallForInterview(
            @PathVariable Long jobApplicationId,
            @RequestBody @Valid JobApplicationRequest jobApplicationRequest
    ) {
        jobService.acceptAndCallForInterview(jobApplicationId, jobApplicationRequest);
        return ResponseEntity.ok("Job Application accepted, Email has been sent successfully to the applicant.");
    }

    @PostMapping("/reject/applicant/{jobApplicationId}")
    public ResponseEntity<String> rejectJobApplicationAndCallForInterview(
            @PathVariable Long jobApplicationId
    ) {
        jobService.rejectCandidateApplication(jobApplicationId);
        return ResponseEntity.ok("Job Application rejected, Email has been sent successfully to the applicant.");
    }

    @GetMapping("/get/pending/applications")
    public ResponseEntity<JobApplicationPageResponse> getAllMyPendingJobApplications(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortBy", defaultValue = "jobTitle", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return ResponseEntity.ok(jobService.getAllMyPendingJobApplications(page, size, sortBy, sortDir));
    }

    @GetMapping("/recommend/employees")
    public ResponseEntity<List<ProfileResponse>> searchForEmployee(
            @RequestBody String text
    ) {
        return ResponseEntity.ok(jobService.searchForEmployee(text));
    }

}
