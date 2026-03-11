package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.profileSummary.request.ProfileSummaryRequest;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;
import com.hire_genie.resume_builder.mapper.ProfileSummaryMapper;
import com.hire_genie.resume_builder.model.ProfileSummary;
import com.hire_genie.resume_builder.repository.ProfileSummaryRepository;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.ProfileSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileSummaryServiceImpl implements ProfileSummaryService {

    private final ProfileSummaryRepository profileSummaryRepository;
    private final ProfileSummaryMapper profileSummaryMapper;
    private final LoggedInUser loggedInUser;

    @Override
    public ProfileSummaryResponse addNewProfileSummary(ProfileSummaryRequest profileSummaryRequest) throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        ProfileSummary profileSummary = profileSummaryRepository.findExistingProfileSummary(userEmail);
        if (profileSummary != null) {
            throw new Exception("Your Profile Summary already exists, Please update it instead of making new Profile Summary.");
        }

        ProfileSummary newUserProfileSummary = profileSummaryMapper.toProfileSummaryFromProfileSummaryRequest(profileSummaryRequest);
        newUserProfileSummary.setIsProfileSummaryDeleted(false);
        newUserProfileSummary.setUserEmail(userEmail);

        return profileSummaryMapper.toProfileSummaryResponseFromProfileSummary(profileSummaryRepository.save(newUserProfileSummary));
    }

    @Override
    public ProfileSummaryResponse getYourProfileSummary() throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        ProfileSummary profileSummary = profileSummaryRepository.findExistingProfileSummary(userEmail);
        if (profileSummary == null) {
            throw new Exception("No profile summary found");
        }

        return profileSummaryMapper.toProfileSummaryResponseFromProfileSummary(profileSummary);
    }

    @Override
    public ProfileSummaryResponse updateProfileSummary(ProfileSummaryRequest profileSummaryRequest) throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        ProfileSummary existingProfileSummary = profileSummaryRepository.findExistingProfileSummary(userEmail);
        if (existingProfileSummary == null) {
            throw new Exception("No profile summary found");
        }

        ProfileSummary updatedProfileSummary = profileSummaryMapper.toProfileSummaryFromProfileSummaryRequest(profileSummaryRequest);

        if (!updatedProfileSummary.getDescription().isEmpty() && !existingProfileSummary.getDescription().equals(updatedProfileSummary.getDescription())) {
            existingProfileSummary.setDescription(updatedProfileSummary.getDescription());
        }
        existingProfileSummary.setIsProfileSummaryDeleted(false);
        existingProfileSummary.setUserEmail(userEmail);

        return profileSummaryMapper.toProfileSummaryResponseFromProfileSummary(profileSummaryRepository.save(existingProfileSummary));
    }

    @Override
    public String deleteProfileSummary() {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        ProfileSummary existingProfileSummary = profileSummaryRepository.findExistingProfileSummary(userEmail);
        if (existingProfileSummary == null) {
            return "There is no Profile Summary associated with your account. Please create one before deleting.";
        }

        existingProfileSummary.setIsProfileSummaryDeleted(true);
        profileSummaryRepository.save(existingProfileSummary);

        return "Profile Summary deleted successfully.";
    }

}
