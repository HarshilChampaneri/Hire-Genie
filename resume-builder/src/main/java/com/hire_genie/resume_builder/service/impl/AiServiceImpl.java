package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.experience.response.ExperienceDescriptionResponse;
import com.hire_genie.resume_builder.dto.other.response.OtherDescriptionResponse;
import com.hire_genie.resume_builder.dto.project.response.ProjectDescriptionResponse;
import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;
import com.hire_genie.resume_builder.exception.ResourceNotFoundException;
import com.hire_genie.resume_builder.model.Experience;
import com.hire_genie.resume_builder.model.Other;
import com.hire_genie.resume_builder.model.Project;
import com.hire_genie.resume_builder.repository.ExperienceRepository;
import com.hire_genie.resume_builder.repository.OtherRepository;
import com.hire_genie.resume_builder.repository.ProjectRepository;
import com.hire_genie.resume_builder.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hire_genie.resume_builder.prompts.system_prompts.ExperienceDescriptionPrompt.experienceDescriptionSystemPrompt;
import static com.hire_genie.resume_builder.prompts.system_prompts.OtherDescriptionPrompt.otherDescriptionSystemPrompt;
import static com.hire_genie.resume_builder.prompts.system_prompts.ProjectDescriptionPrompt.projectDescriptionSystemPrompt;
import static com.hire_genie.resume_builder.prompts.system_prompts.SkillSystemPrompt.skillSystemPrompt;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final ProjectRepository projectRepository;
    private final ExperienceRepository experienceRepository;
    private final OtherRepository otherRepository;

    @Override
    public SkillSummaryResponse provideSkillSummary(String text) {

        BeanOutputConverter<SkillSummaryResponse> converter =
                new BeanOutputConverter<>(SkillSummaryResponse.class);

        String jsonResponse = chatClient
                .prompt()
                .system(skillSystemPrompt)
                .user(text)
                .call()
                .content();

        log.debug("Raw JSON Response: {}", jsonResponse);

        if (jsonResponse != null) {
            return converter.convert(jsonResponse);
        } else {
            return SkillSummaryResponse.builder()
                    .technicalSkills(null)
                    .build();
        }
    }

    @Override
    public ProjectDescriptionResponse rewriteProjectDescriptionWithAi(Long projectId) {

        log.warn("Fetching Project.");
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project", projectId));
        log.warn("Project fetched successfully.");

        if (project.getIsProjectDeleted()) {
            throw new ResourceNotFoundException("project", projectId);
        }

        List<String> projectDescription = project.getProjectDescription();
        log.warn("Fetched Project Description");

        String userProjectDescription = String.join(" ", projectDescription);

        BeanOutputConverter<ProjectDescriptionResponse> converter =
                new BeanOutputConverter<>(ProjectDescriptionResponse.class);

        String userMessage = userProjectDescription + "\n\n" + converter.getFormat();

        String aiProjectDescriptionResponse = chatClient
                .prompt()
                .system(projectDescriptionSystemPrompt)
                .user(userMessage)
                .call()
                .content();

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

        Experience experience = experienceRepository.findById(experienceId).orElseThrow(() ->
                new ResourceNotFoundException("experience", experienceId));

        if (experience.getIsExperienceDeleted()) {
            throw new ResourceNotFoundException("experience", experienceId);
        }

        List<String> experienceDescription = experience.getDescription();

        String userExperienceDescription = String.join(" ", experienceDescription);

        BeanOutputConverter<ExperienceDescriptionResponse> converter =
                new BeanOutputConverter<>(ExperienceDescriptionResponse.class);

        String userMessage = userExperienceDescription + "\n\n" + converter.getFormat();

        String aiExperienceDescriptionResponse = chatClient
                .prompt()
                .system(experienceDescriptionSystemPrompt)
                .user(userMessage)
                .call()
                .content();

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

        Other other = otherRepository.findById(otherId).orElseThrow(() ->
                new ResourceNotFoundException("other", otherId));

        if (other.getIsDeleted()) {
            throw new ResourceNotFoundException("other", otherId);
        }

        List<String> otherDescription = other.getDescription();

        String concatenatedOtherDescription = String.join(" ", otherDescription);

        BeanOutputConverter<OtherDescriptionResponse> converter =
                new BeanOutputConverter<>(OtherDescriptionResponse.class);

        String userMessage = concatenatedOtherDescription + "\n\n" + converter.getFormat();

        String aiResponse = chatClient
                .prompt()
                .system(otherDescriptionSystemPrompt)
                .user(userMessage)
                .call()
                .content();

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
}
