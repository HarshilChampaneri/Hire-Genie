package com.hire_genie.job_service.service.impl;

import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobPageResponse;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import com.hire_genie.job_service.dto.roleplay.RoleplayDTO;
import com.hire_genie.job_service.exception.InvalidAccessException;
import com.hire_genie.job_service.exception.ResourceNotFoundException;
import com.hire_genie.job_service.feignClient.EmployeeRecommendationServiceFeignClient;
import com.hire_genie.job_service.feignClient.JobRecommendationServiceFeignClient;
import com.hire_genie.job_service.feignClient.RoleplayServiceFeignClient;
import com.hire_genie.job_service.kafkaEvent.CandidateProfileEvent;
import com.hire_genie.job_service.kafkaEvent.JobApplicationEvent;
import com.hire_genie.job_service.mapper.JobApplicationMapper;
import com.hire_genie.job_service.mapper.JobMapper;
import com.hire_genie.job_service.model.Company;
import com.hire_genie.job_service.model.Job;
import com.hire_genie.job_service.model.JobApplication;
import com.hire_genie.job_service.repository.CompanyRepository;
import com.hire_genie.job_service.repository.JobApplicationRepository;
import com.hire_genie.job_service.repository.JobRepository;
import com.hire_genie.job_service.security.util.LoggedInUser;
import com.hire_genie.job_service.service.EmailService;
import com.hire_genie.job_service.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hire_genie.job_service.util.StaticConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final JobMapper jobMapper;
    private final LoggedInUser loggedInUser;
    private final JobRecommendationServiceFeignClient jobRecommendationServiceFeignClient;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EmailService emailService;
    private final EmployeeRecommendationServiceFeignClient employeeRecommendationServiceFeignClient;
    private final RoleplayServiceFeignClient roleplayServiceFeignClient;

    @Override
    @Transactional
    @Caching(
            put = @CachePut(value = JOBS, key = "#result.jobId"),
            evict = {
                    @CacheEvict(value = JOBS_PAGE, allEntries = true),
                    @CacheEvict(value = JOBS_BY_COMPANY_PAGE, key = "#companyId + '-*'", allEntries = true)
            }
    )
    public JobResponse addNewJobByCompanyId(Long companyId, JobRequest jobRequest, HttpServletRequest request) {

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

        JobResponse jobResponse = jobMapper.toJobResponseFromJob(jobRepository.save(job));

        String secret = request.getHeader("X-Internal-Secret");
        String email = request.getHeader("X-User-Email");
        String roles = request.getHeader("X-User-Roles");

        pushToJobRecommendationEngine(secret, email, roles, jobResponse);

        return jobResponse;
    }

    @Override
    @Cacheable(
            value = JOBS_PAGE,
            key = "#page + '_' + #size + '_' + #sortBy + '_' + #sortDir"
    )
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
    @Cacheable(
            value = JOBS_BY_COMPANY_PAGE,
            key = "#companyId + '_' + #page + '_' + #size + '_' + #sortBy + '_' + #sortDir"
    )
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
    @Caching(
            put = @CachePut(value = JOBS, key = "#jobId"),
            evict = {
                    @CacheEvict(value = JOBS_PAGE, allEntries = true),
                    @CacheEvict(value = JOBS_BY_COMPANY_PAGE, allEntries = true)
            }
    )
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
    @Caching(
            evict = {
                    @CacheEvict(value = JOBS, key = "#jobId"),
                    @CacheEvict(value = JOBS_PAGE, allEntries = true),
                    @CacheEvict(value = JOBS_BY_COMPANY_PAGE, allEntries = true)
            }
    )
    public String deleteJobById(Long jobId) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Job job = jobRepository.findByJobId(jobId, userEmail).orElseThrow(() ->
                new ResourceNotFoundException("job", jobId));

        job.setIsJobDeleted(true);
        jobRepository.save(job);

        return "Job with jobId: " + jobId + " has been deleted successfully.";
    }

    @Override
    @Transactional
    public void applyForJob(Long jobId) {

        Job job = jobRepository.findByJobIdIgnoringUserEmail(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job", jobId)
        );

        String candidateEmail = loggedInUser.getCurrentLoggedInUser();

        if (jobApplicationRepository.findByJobIdAndCandidateEmail(jobId, candidateEmail).isPresent() || job.getUserEmail().equals(candidateEmail)) {
            throw new InvalidAccessException("You cannot apply for this Job Again!!");
        }

        JobApplication jobApplication = JobApplication.builder()
                .jobId(jobId)
                .candidateEmail(candidateEmail)
                .build();

        JobApplicationEvent jobApplicationEvent = jobApplicationMapper.toJobApplicationEventFromJobApplication(jobApplicationRepository.save(jobApplication));

        kafkaTemplate.send(JOB_APPLICATION_REQUESTS, candidateEmail, jobApplicationEvent);
        log.info("Job application request sent for email: {}", candidateEmail);

    }

    @Override
    public List<ProfileResponse> fetchJobDescriptionAndRecommendEmployees(Long jobId) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Job job = jobRepository.findByJobId(jobId, userEmail).orElseThrow(
                () -> new ResourceNotFoundException("Job", jobId)
        );

        String jobDescription = job.getJobDescription();

        return employeeRecommendationServiceFeignClient.recommendEmployees(jobDescription);

    }

    @Override
    public RoleplayDTO startRoleplay(Long jobId) {

        Job job = jobRepository.findByJobIdIgnoringUserEmail(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job", jobId)
        );

        String jobDescription = job.getJobDescription();

        return roleplayServiceFeignClient.startRoleplay(jobDescription);
    }

    @KafkaListener(topics = CANDIDATE_PROFILE_RESPONSES, groupId = JOB_SERVICE_GROUP)
    public void consumeCandidateProfile(CandidateProfileEvent candidateProfileEvent) {
        Job job = jobRepository.findByJobIdIgnoringUserEmail(candidateProfileEvent.jobId()).orElseThrow(
                () -> new ResourceNotFoundException("Job", candidateProfileEvent.jobId())
        );

        sendCustomEmail(candidateProfileEvent.profile(), job);

    }

    @Async
    protected void pushToJobRecommendationEngine(String secret, String email, String roles, JobResponse jobResponse) {
        try {
            jobRecommendationServiceFeignClient.storeJobProfile(secret, email, roles, jobResponse);
        } catch (Exception e) {
            log.warn("Failed to push Job to Job Recommendation Engine: {}", e.getMessage());
        }
    }

    private void sendCustomEmail(ProfileResponse profileResponse, Job job) {
        log.info("Triggering application confirmation email → {}", profileResponse.email());
        emailService.sendJobApplicationToEmail(profileResponse, job);
    }



}
