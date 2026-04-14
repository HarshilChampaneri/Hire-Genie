package com.hire_genie.job_service.service.impl;

import com.hire_genie.job_service.dto.job.response.JobResponse;
import com.hire_genie.job_service.exception.InvalidAccessException;
import com.hire_genie.job_service.exception.ResourceNotFoundException;
import com.hire_genie.job_service.mapper.JobMapper;
import com.hire_genie.job_service.model.Job;
import com.hire_genie.job_service.repository.JobRepository;
import com.hire_genie.job_service.security.util.LoggedInUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalJobService {

    private final JobRepository jobRepository;
    private final LoggedInUser loggedInUser;
    private final JobMapper jobMapper;

    public String fetchJobDescription(Long jobId) {

        if (!loggedInUser.isRecruiter()) {
            throw new InvalidAccessException("You are Unauthorized to access this service!");
        }

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Job job = jobRepository.findByJobId(jobId, userEmail).orElseThrow(
                () -> new ResourceNotFoundException("Job", jobId)
        );

        return job.getJobDescription();
    }

    public JobResponse fetchJobByJobId(Long jobId) {

        Job job = jobRepository.findByJobIdIgnoringUserEmail(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job", jobId)
        );

        return jobMapper.toJobResponseFromJob(job);
    }
}
