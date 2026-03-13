package com.hire_genie.resume_builder.dto.profile.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record ProfileResponse(

        Long profileId,
        String fullName,
        String profession,
        String email,
        String mobileNo,
        Set<String> urls

) {
}
