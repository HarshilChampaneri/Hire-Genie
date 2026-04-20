package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.profile.request.ProfileRequest;
import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import com.hire_genie.resume_builder.exception.ResourceNotFoundException;
import com.hire_genie.resume_builder.mapper.ProfileMapper;
import com.hire_genie.resume_builder.model.Profile;
import com.hire_genie.resume_builder.repository.ProfileRepository;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import static com.hire_genie.resume_builder.util.StaticConstants.PROFILE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final LoggedInUser loggedInUser;

    @Override
    @CachePut(value = PROFILE, key = "@loggedInUser.getCurrentLoggedInUser()")
    public ProfileResponse addNewProfile(ProfileRequest profileRequest) throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Profile profile = profileMapper.toProfileFromProfileRequest(profileRequest);
        profile.setUserEmail(userEmail);
        profile.setIsProfileDeleted(false);

        Profile existingProfile = profileRepository.findExistingProfile(userEmail);
        if (existingProfile != null) {
            throw new Exception("Your Profile already exists, Please update it instead of making new Profile.");
        }

        return profileMapper.toProfileResponseFromProfile(profileRepository.save(profile));

    }

    @Override
    @Cacheable(value = PROFILE, key = "@loggedInUser.getCurrentLoggedInUser()")
    public ProfileResponse getYourProfile() throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Profile profile = profileRepository.findExistingProfile(userEmail);
        if (profile == null) {
            throw new Exception("No profile found");
        }

        return profileMapper.toProfileResponseFromProfile(profile);

    }

    @Override
    @CachePut(value = PROFILE, key = "@loggedInUser.getCurrentLoggedInUser()")
    public ProfileResponse updateYourProfile(ProfileRequest profileRequest) throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Profile existingProfile = profileRepository.findExistingProfile(userEmail);
        if (existingProfile == null) {
            throw new Exception("No profile found");
        }
        Profile updatedProfile = profileMapper.toProfileFromProfileRequest(profileRequest);

        if (!updatedProfile.getEmail().isEmpty() && !existingProfile.getEmail().equals(updatedProfile.getEmail())) {
            existingProfile.setEmail(updatedProfile.getEmail());
        }
        if (!updatedProfile.getFullName().isEmpty() && !existingProfile.getFullName().equals(updatedProfile.getFullName())) {
            existingProfile.setFullName(updatedProfile.getFullName());
        }
        if (!updatedProfile.getProfession().isEmpty() && !existingProfile.getProfession().equals(updatedProfile.getProfession())) {
            existingProfile.setProfession(updatedProfile.getProfession());
        }
        if (!updatedProfile.getMobileNo().isEmpty() && !existingProfile.getMobileNo().equals(updatedProfile.getMobileNo())) {
            existingProfile.setMobileNo(updatedProfile.getMobileNo());
        }
        if (!updatedProfile.getUrls().isEmpty() && !existingProfile.getUrls().equals(updatedProfile.getUrls())) {
            existingProfile.setUrls(updatedProfile.getUrls());
        }
        existingProfile.setIsProfileDeleted(false);
        existingProfile.setUserEmail(userEmail);

        return profileMapper.toProfileResponseFromProfile(profileRepository.save(existingProfile));

    }

    @Override
    @CacheEvict(value = PROFILE, key = "@loggedInUser.getCurrentLoggedInUser()")
    public String deleteYourProfile() {

        Profile existingProfile = profileRepository.findExistingProfile(loggedInUser.getCurrentLoggedInUser());
        if (existingProfile == null) {
            return "There is no Profile associated with your account. Please create one before deleting.";
        }

        existingProfile.setIsProfileDeleted(true);
        profileRepository.save(existingProfile);

        return "Profile deleted successfully.";

    }

    @Override
    public ProfileResponse getProfileByEmail(String email) throws Exception {

        Profile profile = profileRepository.findExistingProfile(email);
        if (profile == null) {
            throw new Exception("Profile not found with email: " + email);
        }

        return profileMapper.toProfileResponseFromProfile(profile);
    }

}
