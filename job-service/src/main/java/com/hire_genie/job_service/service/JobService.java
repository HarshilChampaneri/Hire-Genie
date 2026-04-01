package com.hire_genie.job_service.service;

import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobPageResponse;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import org.springframework.transaction.annotation.Transactional;

public interface JobService {
    @Transactional
    JobResponse addNewJobByCompanyId(Long companyId, JobRequest jobRequest);

    JobPageResponse getAllJobs(int page, int size, String sortBy, String sortDir);

    JobPageResponse getAllJobsByCompanyId(Long companyId, int page, int size, String sortBy, String sortDir);

    JobResponse updateJobByJobId(Long jobId, JobRequest jobRequest);

    String deleteJobById(Long jobId);
}
