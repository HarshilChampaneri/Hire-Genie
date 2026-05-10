package com.hire_genie.job_service.service;

import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import com.hire_genie.job_service.dto.jobApplication.JobApplicationRequest;
import com.hire_genie.job_service.model.Job;
import com.hire_genie.job_service.model.JobApplication;

public interface EmailService {

    void sendJobApplicationToEmail(ProfileResponse profileResponse, Job job);

    void sendJobApplicationAcceptedToEmail(ProfileResponse profileResponse, JobApplication jobApplication, JobApplicationRequest jobApplicationRequest);

    void sendJobApplicationRejectedToEmail(ProfileResponse profileResponse, Job Job);

}
