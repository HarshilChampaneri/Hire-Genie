package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import com.hire_genie.resume_builder.mapper.ProfileMapper;
import com.hire_genie.resume_builder.model.Profile;
import com.hire_genie.resume_builder.repository.ProfileRepository;
import com.hire_genie.resume_builder.service.InternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalServiceImpl implements InternalService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileResponse fetchProfileResponse(String candidateEmail) throws Exception {

        Profile profile = profileRepository.findExistingProfile(candidateEmail);

        if (profile == null) {
            throw new Exception("Profile not found with candidateEmail: " + candidateEmail);
        }

        return profileMapper.toProfileResponseFromProfile(profile);
    }
}
