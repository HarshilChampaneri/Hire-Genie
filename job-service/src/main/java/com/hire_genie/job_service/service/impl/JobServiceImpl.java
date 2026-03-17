package com.hire_genie.job_service.service.impl;

import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import com.hire_genie.job_service.exception.InvalidAccessException;
import com.hire_genie.job_service.exception.ResourceNotFoundException;
import com.hire_genie.job_service.mapper.JobMapper;
import com.hire_genie.job_service.model.Company;
import com.hire_genie.job_service.model.Job;
import com.hire_genie.job_service.repository.CompanyRepository;
import com.hire_genie.job_service.repository.JobRepository;
import com.hire_genie.job_service.security.util.LoggedInUser;
import com.hire_genie.job_service.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final JobMapper jobMapper;
    private final LoggedInUser loggedInUser;

    @Override
    @Transactional
    public JobResponse addNewJobByCompanyId(Long companyId, JobRequest jobRequest) {

        if (!loggedInUser.isRecruiter()) {
            throw new InvalidAccessException("You are unauthorized to access this service.");
        }

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Company company = companyRepository.findCompanyById(companyId, userEmail);

        Job job = jobMapper.toJobFromJobRequest(jobRequest);
        job.setIsJobDeleted(false);
        job.setUserEmail(userEmail);

        job.setCompany(company);
        company.getJobs().add(job);

        return jobMapper.toJobResponseFromJob(jobRepository.save(job));
    }

    @Override
    public List<JobResponse> getAllJobs() {

        List<Job> jobs = jobRepository.findAllActiveJobs();
        if (jobs.isEmpty()) {
            throw new ResourceNotFoundException("Jobs");
        }

        return jobs.stream()
                .map(jobMapper::toJobResponseFromJob)
                .toList();
    }

    @Override
    public List<JobResponse> getAllJobsByCompanyId(Long companyId) {

        Company company = companyRepository.findByCompanyId(companyId);
        if (company == null) {
            throw new ResourceNotFoundException("company", companyId);
        }

        List<Job> jobs = jobRepository.findAllActiveJobsByCompany(company);
        if (jobs.isEmpty()) {
            throw new ResourceNotFoundException("Jobs");
        }

        return jobs.stream()
                .map(jobMapper::toJobResponseFromJob)
                .toList();
    }

    @Override
    public JobResponse updateJobByJobId(Long jobId, JobRequest jobRequest) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Job job = jobRepository.findByJobId(jobId, userEmail).orElseThrow(() ->
                new ResourceNotFoundException("job", jobId));

        if (!jobRequest.jobDescription().isEmpty() && !jobRequest.jobDescription().equals(job.getJobDescription())) {
            job.setJobDescription(jobRequest.jobDescription());
        }
        if (!jobRequest.jobName().isEmpty() && !jobRequest.jobName().equals(job.getJobName())) {
            job.setJobName(jobRequest.jobName());
        }
        job.setIsJobDeleted(false);

        return jobMapper.toJobResponseFromJob(jobRepository.save(job));
    }

    @Override
    public String deleteJobById(Long jobId) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Job job = jobRepository.findByJobId(jobId, userEmail).orElseThrow(() ->
                new ResourceNotFoundException("job", jobId));

        job.setIsJobDeleted(true);
        jobRepository.save(job);

        return "Job with jobId: " + jobId + " has been deleted successfully.";
    }

}
