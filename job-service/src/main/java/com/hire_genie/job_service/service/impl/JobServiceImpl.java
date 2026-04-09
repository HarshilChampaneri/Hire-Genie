package com.hire_genie.job_service.service.impl;

import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobPageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public JobPageResponse getAllJobs(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Job> jobs = jobRepository.findAllActiveJobs(pageable);
        if (jobs.isEmpty()) {
            throw new ResourceNotFoundException("Jobs");
        }

        return JobPageResponse.builder()
                .jobResponse(jobs.map(jobMapper::toJobResponseFromJob).getContent())
                .isLastPage(jobs.isLast())
                .totalPages(jobs.getTotalPages())
                .totalElements((int) jobs.getTotalElements())
                .pageSize(jobs.getSize())
                .pageIndex(jobs.getNumber())
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();
    }

    @Override
    public JobPageResponse getAllJobsByCompanyId(Long companyId, int page, int size, String sortBy, String sortDir) {

        Company company = companyRepository.findByCompanyId(companyId);
        if (company == null) {
            throw new ResourceNotFoundException("company", companyId);
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Job> jobs = jobRepository.findAllActiveJobsByCompany(company, pageable);
        if (jobs.isEmpty()) {
            throw new ResourceNotFoundException("Jobs");
        }

        return JobPageResponse.builder()
                .jobResponse(jobs.map(jobMapper::toJobResponseFromJob).getContent())
                .isLastPage(jobs.isLast())
                .totalPages(jobs.getTotalPages())
                .totalElements((int) jobs.getTotalElements())
                .pageSize(jobs.getSize())
                .pageIndex(jobs.getNumber())
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();
    }

    @Override
    public JobResponse updateJobByJobId(Long jobId, JobRequest jobRequest) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Job job = jobRepository.findByJobId(jobId, userEmail).orElseThrow(() ->
                new ResourceNotFoundException("job", jobId));

        if (!jobRequest.jobDescription().isEmpty() && !jobRequest.jobDescription().equals(job.getJobDescription())) {
            job.setJobDescription(jobRequest.jobDescription());
        }
        if (!jobRequest.jobTitle().isEmpty() && !jobRequest.jobTitle().equals(job.getJobTitle())) {
            job.setJobTitle(jobRequest.jobTitle());
        }
        if (jobRequest.jobType() != null && !jobRequest.jobType().equals(job.getJobType())) {
            job.setJobType(jobRequest.jobType());
        }
        if (jobRequest.workMode() != null && !jobRequest.workMode().equals(job.getWorkMode())) {
            job.setWorkMode(jobRequest.workMode());
        }
        if (!jobRequest.location().isEmpty() && !jobRequest.location().equals(job.getLocation())) {
            job.setLocation(jobRequest.location());
        }
        if (jobRequest.minSalary() != null && !jobRequest.minSalary().equals(job.getMinSalary())) {
            job.setMinSalary(jobRequest.minSalary());
        }
        if (jobRequest.maxSalary() != null && !jobRequest.maxSalary().equals(job.getMaxSalary())) {
            job.setMaxSalary(jobRequest.maxSalary());
        }
        if (jobRequest.currency() != null && !jobRequest.currency().equals(job.getCurrency())) {
            job.setCurrency(jobRequest.currency());
        }
        if (jobRequest.vacancies() != null && !jobRequest.vacancies().equals(job.getVacancies())) {
            job.setVacancies(jobRequest.vacancies());
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
