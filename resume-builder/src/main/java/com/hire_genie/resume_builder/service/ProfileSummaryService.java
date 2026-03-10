package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.profileSummary.request.ProfileSummaryRequest;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;

public interface ProfileSummaryService {
    ProfileSummaryResponse addNewProfileSummary(ProfileSummaryRequest profileSummaryRequest) throws Exception;

    ProfileSummaryResponse getYourProfileSummary() throws Exception;

    ProfileSummaryResponse updateProfileSummary(ProfileSummaryRequest profileSummaryRequest) throws Exception;

    String deleteProfileSummary();
}
