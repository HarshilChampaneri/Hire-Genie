package com.harshil_infotech.hire_genie.mapper;

import com.harshil_infotech.hire_genie.dto.education.request.EducationRequest;
import com.harshil_infotech.hire_genie.dto.education.response.EducationResponse;
import com.harshil_infotech.hire_genie.model.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public Education toEducationFromEducationRequest (EducationRequest educationRequest) {

        boolean isInProgress = Boolean.TRUE.equals(educationRequest.isEducationInProgress());

        return Education.builder()
                .grades(educationRequest.grades() != null ? educationRequest.grades() : null)
                .gradeTitle(educationRequest.gradeTitle() != null ? educationRequest.gradeTitle() : null)
                .isEducationInProgress(isInProgress)
                .educationTitle(educationRequest.educationTitle() != null ? educationRequest.educationTitle() : null)
                .startDate(educationRequest.startDate() != null ? educationRequest.startDate() : null)
                .endDate(isInProgress ? null : educationRequest.endDate())
                .fieldOfStudy(educationRequest.fieldOfStudy() != null ? educationRequest.fieldOfStudy() : null)
                .location(educationRequest.location() != null ? educationRequest.location() : null)
                .build();
    }

    public EducationResponse toEducationResponseFromEducation(Education education) {

        return EducationResponse.builder()
                .educationId(education.getEducationId())
                .isEducationInProgress(education.getIsEducationInProgress())
                .educationTitle(education.getEducationTitle())
                .fieldOfStudy(education.getFieldOfStudy())
                .grades(education.getGrades())
                .location(education.getLocation())
                .gradeTitle(education.getGradeTitle())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .build();
    }

}
