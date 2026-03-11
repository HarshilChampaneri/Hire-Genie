package com.hire_genie.resume_builder.mapper;

import com.hire_genie.resume_builder.dto.profileSummary.request.ProfileSummaryRequest;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;
import com.hire_genie.resume_builder.model.ProfileSummary;
import org.springframework.stereotype.Component;

@Component
public class ProfileSummaryMapper {

    public ProfileSummary toProfileSummaryFromProfileSummaryRequest(ProfileSummaryRequest profileSummaryRequest) {
        return ProfileSummary.builder()
                .description(profileSummaryRequest.profileSummary() != null ? profileSummaryRequest.profileSummary() : null)
                .build();
    }

    public ProfileSummaryResponse toProfileSummaryResponseFromProfileSummary(ProfileSummary profileSummary) {
        return ProfileSummaryResponse.builder()
                .profileSummaryId(profileSummary.getProfileSummaryId())
                .profileSummary(profileSummary.getDescription())
                .build();
    }

}
