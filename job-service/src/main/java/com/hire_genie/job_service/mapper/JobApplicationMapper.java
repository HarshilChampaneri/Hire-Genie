package com.hire_genie.job_service.mapper;

import com.hire_genie.job_service.kafkaEvent.JobApplicationEvent;
import com.hire_genie.job_service.model.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {

    public JobApplicationEvent toJobApplicationEventFromJobApplication(JobApplication jobApplication) {
        return JobApplicationEvent.builder()
                .jobId(jobApplication.getJobId())
                .candidateEmail(jobApplication.getCandidateEmail())
                .build();
    }

}
