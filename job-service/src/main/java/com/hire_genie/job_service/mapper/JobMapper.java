package com.hire_genie.job_service.mapper;

import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import com.hire_genie.job_service.model.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job toJobFromJobRequest(JobRequest jobRequest) {

        return Job.builder()
                .jobTitle(!jobRequest.jobTitle().isEmpty() ? jobRequest.jobTitle() : null)
                .jobDescription(!jobRequest.jobDescription().isEmpty() ? jobRequest.jobDescription() : null)
                .workMode(jobRequest.workMode())
                .jobType(jobRequest.jobType())
                .location(!jobRequest.location().isEmpty() ? jobRequest.location() : null)
                .minSalary(jobRequest.minSalary() != null ? jobRequest.minSalary() : null)
                .maxSalary(jobRequest.maxSalary() != null ? jobRequest.maxSalary() : null)
                .currency(jobRequest.currency() != null ? jobRequest.currency() : null)
                .vacancies(jobRequest.vacancies() != null ? jobRequest.vacancies() : null)
                .build();

    }

    public JobResponse toJobResponseFromJob(Job job) {

        return JobResponse.builder()
                .jobId(job.getJobId() != null ? job.getJobId() : null)
                .jobTitle(!job.getJobTitle().isEmpty() ? job.getJobTitle() : null)
                .jobDescription(!job.getJobDescription().isEmpty() ? job.getJobDescription() : null)
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .location(!job.getLocation().isEmpty() ? job.getLocation() : null)
                .minSalary(job.getMinSalary() != null ? job.getMinSalary() : null)
                .maxSalary(job.getMaxSalary() != null ? job.getMaxSalary() : null)
                .currency(job.getCurrency() != null ? job.getCurrency() : null)
                .vacancies(job.getVacancies() != null ? job.getVacancies() : null)
                .build();

    }

}
