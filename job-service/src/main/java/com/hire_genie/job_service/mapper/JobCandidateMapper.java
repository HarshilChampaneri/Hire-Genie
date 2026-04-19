package com.hire_genie.job_service.mapper;

import com.hire_genie.job_service.kafkaEvent.JobCandidateEvent;
import com.hire_genie.job_service.model.JobCandidate;
import org.springframework.stereotype.Component;

@Component
public class JobCandidateMapper {

    public JobCandidateEvent toJobCandidateEventFromJobCandidate(JobCandidate jobCandidate) {
        return JobCandidateEvent.builder()
                .jobId(jobCandidate.getJobId())
                .candidateEmail(jobCandidate.getCandidateEmail())
                .build();
    }

}
