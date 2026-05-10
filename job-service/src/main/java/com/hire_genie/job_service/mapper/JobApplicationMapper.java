package com.hire_genie.job_service.mapper;

import com.hire_genie.job_service.dto.jobApplication.JobApplicationResponse;
import com.hire_genie.job_service.kafkaEvent.JobApplicationEvent;
import com.hire_genie.job_service.model.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {

    public JobApplicationEvent toJobApplicationEventFromJobApplication(JobApplication jobApplication) {
        return JobApplicationEvent.builder()
                .jobId(jobApplication.getJob().getJobId())
                .candidateEmail(jobApplication.getCandidateEmail())
                .build();
    }

    public JobApplicationResponse toJobApplicationResponseFromJobApplication(JobApplication jobApplication) {
        return JobApplicationResponse.builder()
                .id(jobApplication.getId())
                .jobTitle(jobApplication.getJobTitle())
                .candidateEmail(jobApplication.getCandidateEmail())
                .recruiterEmail(jobApplication.getRecruiterEmail())
                .companyName(jobApplication.getCompanyName())
                .build();
    }

}
