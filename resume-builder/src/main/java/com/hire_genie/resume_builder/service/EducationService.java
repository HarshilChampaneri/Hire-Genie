package com.hire_genie.resume_builder.service;


import com.hire_genie.resume_builder.dto.education.request.EducationRequest;
import com.hire_genie.resume_builder.dto.education.request.EducationRequestList;
import com.hire_genie.resume_builder.dto.education.response.EducationResponse;
import com.hire_genie.resume_builder.dto.education.response.EducationResponseList;

import java.util.List;

public interface EducationService {
    List<EducationResponse> addEducations (EducationRequestList educationRequestList);

    EducationResponseList getAllEducations() throws Exception;

    EducationResponse getEducationById(Long educationId);

    EducationResponse updateEducation(Long educationId, EducationRequest educationRequest);

    String deleteEducation(Long educationId);
}
