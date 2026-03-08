package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.experience.request.ExperienceRequest;
import com.hire_genie.resume_builder.dto.experience.request.ExperienceRequestList;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponse;
import com.hire_genie.resume_builder.dto.experience.response.ExperienceResponseList;
import com.hire_genie.resume_builder.exception.EndDateRequiredException;
import com.hire_genie.resume_builder.exception.InvalidDateRangeException;
import com.hire_genie.resume_builder.exception.ResourceNotFoundException;
import com.hire_genie.resume_builder.mapper.ExperienceMapper;
import com.hire_genie.resume_builder.model.Experience;
import com.hire_genie.resume_builder.repository.ExperienceRepository;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;
    private final LoggedInUser loggedInUser;

    @Override
    public List<ExperienceResponse> addExperiences(ExperienceRequestList experienceRequestList) {

        List<Experience> experiences = experienceRequestList
                .experienceRequests()
                .stream()
                .map(experienceMapper::toExperienceFromExperienceRequest)
                .toList();

        for (Experience experience : experiences) {
            if (!experience.getIsWorkingInCompany()) {
                if (experience.getEndDate() == null) {
                    throw new EndDateRequiredException("experience");
                }
                if (experience.getEndDate().isBefore(experience.getStartDate())) {
                    throw new InvalidDateRangeException("experience");
                }
            }
            experience.setIsExperienceDeleted(false);
            experience.setUserEmail(loggedInUser.getCurrentLoggedInUser());
        }

        return experienceRepository.saveAll(experiences)
                .stream()
                .map(experienceMapper::toExperienceResponseFromExperience)
                .toList();

    }

    @Override
    public ExperienceResponseList getAllExperiences() throws Exception {

        List<Experience> experiences = experienceRepository.findActiveExperiences(loggedInUser.getCurrentLoggedInUser());

        if (experiences.isEmpty()) {
            throw new Exception("No Experiences Found");
        }

        List<ExperienceResponse> experienceResponseList = experiences.stream()
//                .filter(experience -> Boolean.FALSE.equals(experience.getIsExperienceDeleted()))
                .map(experienceMapper::toExperienceResponseFromExperience)
                .toList();

        return ExperienceResponseList.builder()
                .experiences(experienceResponseList)
                .build();

    }

    @Override
    public ExperienceResponse getExperienceById(Long experienceId) {

        Experience experience = experienceRepository.findByExperienceIdAndUserEmail(
                experienceId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("experience", experienceId));

        if (experience.getIsExperienceDeleted()) {
            throw new ResourceNotFoundException("experience", experienceId);
        }

        return experienceMapper.toExperienceResponseFromExperience(experience);
    }

    @Override
    public ExperienceResponse updateExperience(Long experienceId, ExperienceRequest experienceRequest) {

        Experience experience = experienceRepository.findByExperienceIdAndUserEmail(
                experienceId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("experience", experienceId));

        if (experience.getIsExperienceDeleted()) {
            throw new ResourceNotFoundException("experience", experienceId);
        }

        Experience mappedExperience = experienceMapper.toExperienceFromExperienceRequest(experienceRequest);

        if (mappedExperience.getIsWorkingInCompany() != null && !experience.getIsWorkingInCompany().equals(mappedExperience.getIsWorkingInCompany())) {
            experience.setIsExperienceDeleted(mappedExperience.getIsWorkingInCompany());
        }
        if (!mappedExperience.getDescription().isEmpty() && !experience.getDescription().equals(mappedExperience.getDescription())) {
            experience.setDescription(mappedExperience.getDescription());
        }
        if (mappedExperience.getPosition() != null && !experience.getPosition().equals(mappedExperience.getPosition())) {
            experience.setPosition(mappedExperience.getPosition());
        }
        if (mappedExperience.getCompanyName() != null && !experience.getCompanyName().equals(mappedExperience.getCompanyName())) {
            experience.setCompanyName(mappedExperience.getCompanyName());
        }
        if (mappedExperience.getStartDate() != null && !experience.getStartDate().equals(mappedExperience.getStartDate())) {
            experience.setStartDate(mappedExperience.getStartDate());
        }
        if (mappedExperience.getEndDate() != null && !experience.getEndDate().equals(mappedExperience.getEndDate())) {
            experience.setEndDate(mappedExperience.getEndDate());
        }
        experience.setIsExperienceDeleted(false);

        experience = experienceRepository.save(experience);

        return experienceMapper.toExperienceResponseFromExperience(experience);
    }

    @Override
    public String deleteExperience(Long experienceId) {

        Experience experience = experienceRepository.findByExperienceIdAndUserEmail(
                experienceId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("experience", experienceId));

        if (experience.getIsExperienceDeleted()) {
            return "Experience with experienceId: " + experienceId + " is already deleted Before.";
        }

        experience.setIsExperienceDeleted(true);
        experienceRepository.save(experience);

        return "Experience with experienceId: " + experienceId + " deleted successfully.";
    }
}
