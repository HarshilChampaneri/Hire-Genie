package com.hire_genie.resume_builder.mapper;

import com.hire_genie.resume_builder.dto.profile.request.ProfileRequest;
import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import com.hire_genie.resume_builder.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public Profile toProfileFromProfileRequest(ProfileRequest profileRequest) {

        return Profile.builder()
                .urls(!profileRequest.urls().isEmpty() ? profileRequest.urls() : null)
                .email(profileRequest.email() != null ? profileRequest.email() : null)
                .fullName(profileRequest.fullName() != null ? profileRequest.fullName() : null)
                .profession(profileRequest.profession() != null ? profileRequest.profession() : null)
                .mobileNo(profileRequest.mobileNo() != null ? profileRequest.mobileNo() : null)
                .build();

    }

    public ProfileResponse toProfileResponseFromProfile(Profile profile) {

        return ProfileResponse.builder()
                .profileId(profile.getProfileId())
                .urls(profile.getUrls())
                .email(profile.getEmail())
                .mobileNo(profile.getMobileNo())
                .profession(profile.getProfession())
                .fullName(profile.getFullName())
                .build();

    }
}
