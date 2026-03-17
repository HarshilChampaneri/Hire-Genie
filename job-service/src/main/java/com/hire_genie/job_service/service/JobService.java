package com.hire_genie.job_service.service;

import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobService {
    @Transactional
    JobResponse addNewJobByCompanyId(Long companyId, JobRequest jobRequest);

    List<JobResponse> getAllJobs();

    List<JobResponse> getAllJobsByCompanyId(Long companyId);

    JobResponse updateJobByJobId(Long jobId, JobRequest jobRequest);

    String deleteJobById(Long jobId);
}
