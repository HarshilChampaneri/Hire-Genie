package com.hire_genie.job_service.service;

import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import com.hire_genie.job_service.model.Job;

public interface EmailService {

    void sendJobApplicationToEmail(ProfileResponse profileResponse, Job job);

}
