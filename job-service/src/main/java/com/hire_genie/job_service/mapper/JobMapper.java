package com.hire_genie.job_service.mapper;

import com.hire_genie.job_service.dto.job.request.JobRequest;
import com.hire_genie.job_service.dto.job.response.JobResponse;
import com.hire_genie.job_service.model.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job toJobFromJobRequest(JobRequest jobRequest) {

        return Job.builder()
                .jobName(!jobRequest.jobName().isEmpty() ? jobRequest.jobName() : null)
                .jobDescription(!jobRequest.jobDescription().isEmpty() ? jobRequest.jobDescription() : null)
                .build();

    }

    public JobResponse toJobResponseFromJob(Job job) {

        return JobResponse.builder()
                .jobId(job.getJobId() != null ? job.getJobId() : null)
                .jobName(!job.getJobName().isEmpty() ? job.getJobName() : null)
                .jobDescription(!job.getJobDescription().isEmpty() ? job.getJobDescription() : null)
                .build();

    }

}
