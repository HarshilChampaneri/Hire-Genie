package com.harshil_infotech.hire_genie.mapper;

import com.harshil_infotech.hire_genie.dto.experience.request.ExperienceRequest;
import com.harshil_infotech.hire_genie.dto.experience.response.ExperienceResponse;
import com.harshil_infotech.hire_genie.model.Experience;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {

    public Experience toExperienceFromExperienceRequest(ExperienceRequest experienceRequest) {

        boolean inProgress = Boolean.TRUE.equals(experienceRequest.isWorkingInCompany());

        return Experience.builder()
                .companyName(experienceRequest.companyName() != null ? experienceRequest.companyName() : null)
                .description(!experienceRequest.description().isEmpty() ? experienceRequest.description() : null)
                .isWorkingInCompany(inProgress)
                .position(experienceRequest.position() != null ? experienceRequest.position() : null)
                .endDate(inProgress ? null : experienceRequest.endDate())
                .startDate(experienceRequest.startDate() != null ? experienceRequest.startDate() : null)
                .build();

    }

    public ExperienceResponse toExperienceResponseFromExperience(Experience experience) {

        return ExperienceResponse.builder()
                .experienceId(experience.getExperienceId())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .description(experience.getDescription())
                .isWorkingInCompany(experience.getIsWorkingInCompany())
                .companyName(experience.getCompanyName())
                .position(experience.getPosition())
                .build();

    }

}
