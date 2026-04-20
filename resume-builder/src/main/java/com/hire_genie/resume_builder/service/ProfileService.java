package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.profile.request.ProfileRequest;
import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse addNewProfile(ProfileRequest profileRequest) throws Exception;

    ProfileResponse getYourProfile() throws Exception;

    ProfileResponse updateYourProfile(ProfileRequest profileRequest) throws Exception;

    String deleteYourProfile() throws Exception;

    ProfileResponse getProfileByEmail(String email) throws Exception;

}
