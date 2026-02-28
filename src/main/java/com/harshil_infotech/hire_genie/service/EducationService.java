package com.harshil_infotech.hire_genie.service;


import com.harshil_infotech.hire_genie.dto.education.request.EducationRequest;
import com.harshil_infotech.hire_genie.dto.education.request.EducationRequestList;
import com.harshil_infotech.hire_genie.dto.education.response.EducationResponse;
import com.harshil_infotech.hire_genie.dto.education.response.EducationResponseList;

import java.util.List;

public interface EducationService {
    List<EducationResponse> addEducations (EducationRequestList educationRequestList);

    EducationResponseList getAllEducations() throws Exception;

    EducationResponse getEducationById(Long educationId);

    EducationResponse updateEducation(Long educationId, EducationRequest educationRequest);

    String deleteEducation(Long educationId);
}
