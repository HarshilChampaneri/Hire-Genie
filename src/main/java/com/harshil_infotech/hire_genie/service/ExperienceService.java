package com.harshil_infotech.hire_genie.service;

import com.harshil_infotech.hire_genie.dto.experience.request.ExperienceRequest;
import com.harshil_infotech.hire_genie.dto.experience.request.ExperienceRequestList;
import com.harshil_infotech.hire_genie.dto.experience.response.ExperienceResponse;
import com.harshil_infotech.hire_genie.dto.experience.response.ExperienceResponseList;

import java.util.List;

public interface ExperienceService {
    List<ExperienceResponse> addExperiences(ExperienceRequestList experienceRequestList);

    ExperienceResponseList getAllExperiences() throws Exception;

    ExperienceResponse getExperienceById(Long experienceId);

    ExperienceResponse updateExperience(Long experienceId, ExperienceRequest experienceRequest);

    String deleteExperience(Long experienceId);
}
