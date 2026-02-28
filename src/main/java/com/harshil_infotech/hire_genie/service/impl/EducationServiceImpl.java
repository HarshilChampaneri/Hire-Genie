package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.dto.education.request.EducationRequest;
import com.harshil_infotech.hire_genie.dto.education.request.EducationRequestList;
import com.harshil_infotech.hire_genie.dto.education.response.EducationResponse;
import com.harshil_infotech.hire_genie.dto.education.response.EducationResponseList;
import com.harshil_infotech.hire_genie.exception.EndDateRequiredException;
import com.harshil_infotech.hire_genie.exception.InvalidDateRangeException;
import com.harshil_infotech.hire_genie.exception.ResourceNotFoundException;
import com.harshil_infotech.hire_genie.mapper.EducationMapper;
import com.harshil_infotech.hire_genie.model.Education;
import com.harshil_infotech.hire_genie.repository.EducationRepository;
import com.harshil_infotech.hire_genie.service.EducationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;

    @Override
    public List<EducationResponse> addEducations(EducationRequestList educationRequestList) {

        List<Education> educations = educationRequestList
                .educationRequests()
                .stream()
                .map(educationMapper::toEducationFromEducationRequest)
                .toList();

        for (Education education : educations) {
            if (!education.getIsEducationInProgress()) {
                if (education.getEndDate() == null) {
                    throw new EndDateRequiredException("education");
                }
                if (education.getEndDate().isBefore(education.getStartDate())) {
                    throw new InvalidDateRangeException("education");
                }
            }
            education.setIsEducationDeleted(false);
        }

        return educationRepository.saveAll(educations)
                .stream()
                .map(educationMapper::toEducationResponseFromEducation)
                .toList();

    }

    @Override
    public EducationResponseList getAllEducations() throws Exception {

        List<Education> educations = educationRepository.findAll();

        if (educations.isEmpty()) {
            throw new Exception("No educations Found");
        }

        List<EducationResponse> educationResponseList = educations.stream()
                .filter(education -> Boolean.FALSE.equals(education.getIsEducationDeleted()))
                .map(educationMapper::toEducationResponseFromEducation)
                .toList();

        return EducationResponseList.builder()
                .educations(educationResponseList)
                .build();

    }

    @Override
    public EducationResponse getEducationById(Long educationId) {
        Education education = educationRepository.findById(educationId).orElseThrow(() ->
                new ResourceNotFoundException("education", educationId));

        if (education.getIsEducationDeleted()) {
            throw new ResourceNotFoundException("education", educationId);
        }

        return educationMapper.toEducationResponseFromEducation(education);
    }

    @Override
    public EducationResponse updateEducation(Long educationId, EducationRequest educationRequest) {

        Education education = educationRepository.findById(educationId).orElseThrow(() ->
                new ResourceNotFoundException("education", educationId));

        if (education.getIsEducationDeleted()) {
            throw new ResourceNotFoundException("education", educationId);
        }

        Education mappedEducation = educationMapper.toEducationFromEducationRequest(educationRequest);

        if (mappedEducation.getEducationTitle() != null && !education.getEducationTitle().equals(mappedEducation.getEducationTitle())) {
            education.setEducationTitle(mappedEducation.getEducationTitle());
        }
        if (mappedEducation.getGrades() != null && !education.getGrades().equals(mappedEducation.getGrades())) {
            education.setGrades(mappedEducation.getGrades());
        }
        if (mappedEducation.getLocation() != null && !education.getLocation().equals(mappedEducation.getLocation())) {
            education.setLocation(mappedEducation.getLocation());
        }
        if (mappedEducation.getStartDate() != null && !education.getStartDate().equals(mappedEducation.getStartDate())) {
            education.setStartDate(mappedEducation.getStartDate());
        }
        if (mappedEducation.getEndDate() != null && !education.getEndDate().equals(mappedEducation.getEndDate())) {
            education.setEndDate(mappedEducation.getEndDate());
        }
        if (mappedEducation.getIsEducationInProgress() != null && !education.getIsEducationInProgress().equals(mappedEducation.getIsEducationInProgress())) {
            education.setIsEducationInProgress(mappedEducation.getIsEducationInProgress());
        }
        if (mappedEducation.getFieldOfStudy() != null && !education.getFieldOfStudy().equals(mappedEducation.getFieldOfStudy())) {
            education.setEducationTitle(mappedEducation.getFieldOfStudy());
        }
        if (mappedEducation.getGradeTitle() != null && !education.getGradeTitle().equals(mappedEducation.getGradeTitle())) {
            education.setGradeTitle(mappedEducation.getGradeTitle());
        }
        education.setIsEducationDeleted(false);

        education = educationRepository.save(education);

        return educationMapper.toEducationResponseFromEducation(education);
    }

    @Override
    public String deleteEducation(Long educationId) {

        Education education = educationRepository.findById(educationId).orElseThrow(() ->
                new ResourceNotFoundException("education", educationId));

        if (education.getIsEducationDeleted()) {
            return "Education with educationId: " + educationId + " is already deleted Before.";
        }

        education.setIsEducationDeleted(true);
        educationRepository.save(education);

        return "Education with educationId: " + educationId + " deleted successfully.";
    }
}
