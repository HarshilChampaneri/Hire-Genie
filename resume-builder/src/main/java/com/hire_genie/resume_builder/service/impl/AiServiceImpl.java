package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.experience.response.ExperienceDescriptionResponse;
import com.hire_genie.resume_builder.dto.other.response.OtherDescriptionResponse;
import com.hire_genie.resume_builder.dto.profileSummary.response.ProfileSummaryResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectDescriptionResponse;
import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;
import com.hire_genie.resume_builder.exception.ResourceNotFoundException;
import com.hire_genie.resume_builder.mapper.ProfileSummaryMapper;
import com.hire_genie.resume_builder.model.*;
import com.hire_genie.resume_builder.repository.*;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hire_genie.resume_builder.prompts.system_prompts.ExperienceDescriptionPrompt.experienceDescriptionSystemPrompt;
import static com.hire_genie.resume_builder.prompts.system_prompts.OtherDescriptionPrompt.otherDescriptionSystemPrompt;
import static com.hire_genie.resume_builder.prompts.system_prompts.ProfileSummaryPrompt.profileSummarySystemPrompt;
import static com.hire_genie.resume_builder.prompts.system_prompts.ProjectDescriptionPrompt.projectDescriptionSystemPrompt;
import static com.hire_genie.resume_builder.prompts.system_prompts.SkillSystemPrompt.skillSystemPrompt;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final SkillRepository skillRepository;
    private final ProjectRepository projectRepository;
    private final ExperienceRepository experienceRepository;
    private final OtherRepository otherRepository;
    private final ProfileSummaryRepository profileSummaryRepository;
    private final ProfileSummaryMapper profileSummaryMapper;
    private final LoggedInUser loggedInUser;

    @Override
    public SkillSummaryResponse provideSkillSummary(String text) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Skill skill = skillRepository.findActiveSkill(userEmail);

        BeanOutputConverter<SkillSummaryResponse> converter =
                new BeanOutputConverter<>(SkillSummaryResponse.class);

        String jsonResponse = generateAIResponses(skillSystemPrompt, text);

        log.debug("Raw JSON Response: {}", jsonResponse);

        if (jsonResponse != null) {
            SkillSummaryResponse response = converter.convert(jsonResponse);

            if (skill != null) {
                skill.setSkills(response.technicalSkills());
                skill.setIsSkillDeleted(false);
                skill.setUserEmail(userEmail);
                skillRepository.save(skill);
            } else {
                Skill newSkill = Skill.builder()
                        .skills(response.technicalSkills())
                        .isSkillDeleted(false)
                        .userEmail(userEmail)
                        .build();
                skillRepository.save(newSkill);
            }

            return response;
        } else {
            return SkillSummaryResponse.builder()
                    .technicalSkills(null)
                    .build();
        }
    }

    @Override
    public ProjectDescriptionResponse rewriteProjectDescriptionWithAi(Long projectId) {

        String email = loggedInUser.getCurrentLoggedInUser();
        log.warn("Fetching Project ID: {} for Email: {}", projectId, email);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        if (!project.getUserEmail().equals(email) || project.getIsProjectDeleted()) {
            throw new ResourceNotFoundException("Access Denied or Deleted", projectId);
        }

        List<String> projectDescription = project.getProjectDescription();
        log.warn("Fetched Project Description");

        String userProjectDescription = String.join(" ", projectDescription);

        BeanOutputConverter<ProjectDescriptionResponse> converter =
                new BeanOutputConverter<>(ProjectDescriptionResponse.class);

        String userMessage = userProjectDescription + "\n\n" + converter.getFormat();

        String aiProjectDescriptionResponse = generateAIResponses(projectDescriptionSystemPrompt, userMessage);

        log.debug("Raw ai response: {}", aiProjectDescriptionResponse);

        if (aiProjectDescriptionResponse != null) {
            ProjectDescriptionResponse projectDescriptionResponse =
                    converter.convert(aiProjectDescriptionResponse);
            List<String> finalResponse = projectDescriptionResponse.projectDescription();

            log.warn("Returning final Response");
            project.setProjectDescription(finalResponse);
            projectRepository.save(project);

            return projectDescriptionResponse;
        }

        return ProjectDescriptionResponse.builder()
                .projectDescription(null)
                .build();
    }

    @Override
    public ExperienceDescriptionResponse rewriteExperienceDescriptionWithAi(Long experienceId) {

        Experience experience = experienceRepository.findByExperienceIdAndUserEmail(
                experienceId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("experiences", experienceId));

        if (experience.getIsExperienceDeleted()) {
            throw new ResourceNotFoundException("experiences", experienceId);
        }

        List<String> experienceDescription = experience.getDescription();

        String userExperienceDescription = String.join(" ", experienceDescription);

        BeanOutputConverter<ExperienceDescriptionResponse> converter =
                new BeanOutputConverter<>(ExperienceDescriptionResponse.class);

        String userMessage = userExperienceDescription + "\n\n" + converter.getFormat();

        String aiExperienceDescriptionResponse = generateAIResponses(experienceDescriptionSystemPrompt, userMessage);

        if (aiExperienceDescriptionResponse != null) {
            ExperienceDescriptionResponse experienceDescriptionResponse =
                    converter.convert(aiExperienceDescriptionResponse);
            List<String> finalResponse = experienceDescriptionResponse.experienceDescription();

            log.warn("Returning final Response");
            experience.setDescription(finalResponse);
            experienceRepository.save(experience);

            return experienceDescriptionResponse;
        }

        return ExperienceDescriptionResponse.builder()
                .experienceDescription(null)
                .build();
    }

    @Override
    public OtherDescriptionResponse rewriteOtherSectionDescriptionWithAi(Long otherId) {

        Other other = otherRepository.findByOtherIdAndUserEmail(
                otherId,
                loggedInUser.getCurrentLoggedInUser()
        ).orElseThrow(() -> new ResourceNotFoundException("other", otherId));

        if (other.getIsDeleted()) {
            throw new ResourceNotFoundException("other", otherId);
        }

        List<String> otherDescription = other.getDescription();

        String concatenatedOtherDescription = String.join(" ", otherDescription);

        BeanOutputConverter<OtherDescriptionResponse> converter =
                new BeanOutputConverter<>(OtherDescriptionResponse.class);

        String userMessage = concatenatedOtherDescription + "\n\n" + converter.getFormat();

        String aiResponse = generateAIResponses(otherDescriptionSystemPrompt, userMessage);

        if (aiResponse != null) {
            OtherDescriptionResponse otherDescriptionResponse = converter.convert(aiResponse);
            List<String> finalResponse = otherDescriptionResponse.otherDescription();

            other.setDescription(finalResponse);
            otherRepository.save(other);

            return otherDescriptionResponse;
        }

        return OtherDescriptionResponse.builder()
                .otherDescription(null)
                .build();
    }

    @Override
    public ProfileSummaryResponse rewriteProfileSummaryWithAi() throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        ProfileSummary profileSummary = profileSummaryRepository.findExistingProfileSummary(userEmail);
        if (profileSummary == null) {
            throw new Exception("No Profile Summary found. Please Proceed with Generate Profile Summary.");
        }

        String userProfileSummary = profileSummary.getDescription();

        String aiResponse = generateAIResponses(profileSummarySystemPrompt, userProfileSummary);

        profileSummary.setDescription(aiResponse);
        profileSummary.setUserEmail(userEmail);
        profileSummary.setIsProfileSummaryDeleted(false);

        return profileSummaryMapper.toProfileSummaryResponseFromProfileSummary(
                profileSummaryRepository.save(profileSummary)
        );
    }

    @Override
    public ProfileSummaryResponse generateProfileSummaryWithAi() throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        ProfileSummary profileSummary = profileSummaryRepository.findExistingProfileSummary(userEmail);
        if (profileSummary != null) {
            throw new Exception("You already have a profile summary created. Please proceed with rewrite profile summary with AI.");
        }

        List<Experience> experienceList = experienceRepository.findActiveExperiences(userEmail);
        List<Project> projectList = projectRepository.findActiveProjects(userEmail);
        Skill skill = skillRepository.findActiveSkill(userEmail);

        if (experienceList.isEmpty() && projectList.isEmpty() && skill == null) {
            throw new Exception("Please add Experience, or Projects, or Skills first to generate the profile summary.");
        }

        String combinedString = "Experience: " + experienceList
                + "Projects: " + projectList
                + "Skills: " + skill;

        String aiResponse = generateAIResponses(profileSummarySystemPrompt, combinedString);

        ProfileSummary newProfileSummary = ProfileSummary.builder()
                .description(aiResponse)
                .userEmail(userEmail)
                .isProfileSummaryDeleted(false)
                .build();

        return profileSummaryMapper.toProfileSummaryResponseFromProfileSummary(
                profileSummaryRepository.save(newProfileSummary)
        );
    }

    private String generateAIResponses(String systemPrompt, String userPrompt) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }

}
