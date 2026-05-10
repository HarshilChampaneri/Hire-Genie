package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;

public interface InternalService {

    ProfileResponse fetchProfileResponse(String candidateEmail) throws Exception;

}
