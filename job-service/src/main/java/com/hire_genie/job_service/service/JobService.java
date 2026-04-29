package com.hire_genie.job_service.service;

import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobPageResponse;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import com.hire_genie.job_service.dto.roleplay.RoleplayDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobService {
    @Transactional
    JobResponse addNewJobByCompanyId(Long companyId, JobRequest jobRequest, HttpServletRequest request);

    JobPageResponse getAllJobs(int page, int size, String sortBy, String sortDir);

    JobPageResponse getAllJobsByCompanyId(Long companyId, int page, int size, String sortBy, String sortDir);

    JobResponse updateJobByJobId(Long jobId, JobRequest jobRequest);

    String deleteJobById(Long jobId);

    void applyForJob(Long jobId);

    List<ProfileResponse> fetchJobDescriptionAndRecommendEmployees(Long jobId);

    RoleplayDTO startRoleplay(Long jobId);

}
