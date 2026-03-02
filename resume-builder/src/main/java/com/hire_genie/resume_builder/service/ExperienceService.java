package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.experience.request.ExperienceRequest;
import com.hire_genie.resume_builder.dto.experience.request.ExperienceRequestList;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponse;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponseList;

import java.util.List;

public interface ExperienceService {
    List<ExperienceResponse> addExperiences(ExperienceRequestList experienceRequestList);

    ExperienceResponseList getAllExperiences() throws Exception;

    ExperienceResponse getExperienceById(Long experienceId);

    ExperienceResponse updateExperience(Long experienceId, ExperienceRequest experienceRequest);

    String deleteExperience(Long experienceId);
}
