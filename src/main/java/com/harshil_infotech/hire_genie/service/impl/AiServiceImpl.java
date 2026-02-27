package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.dto.experience.response.ExperienceDescriptionResponse;
import com.harshil_infotech.hire_genie.dto.project.response.ProjectDescriptionResponse;
import com.harshil_infotech.hire_genie.dto.skill_summary.response.SkillSummaryResponse;
import com.harshil_infotech.hire_genie.exception.ResourceNotFoundException;
import com.harshil_infotech.hire_genie.model.Experience;
import com.harshil_infotech.hire_genie.model.Project;
import com.harshil_infotech.hire_genie.repository.ExperienceRepository;
import com.harshil_infotech.hire_genie.repository.ProjectRepository;
import com.harshil_infotech.hire_genie.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.harshil_infotech.hire_genie.prompts.system_prompts.ExperienceDescriptionPrompt.experienceDescriptionSystemPrompt;
import static com.harshil_infotech.hire_genie.prompts.system_prompts.ProjectDescriptionPrompt.projectDescriptionSystemPrompt;
import static com.harshil_infotech.hire_genie.prompts.system_prompts.SkillSystemPrompt.skillSystemPrompt;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final ProjectRepository projectRepository;
    private final ExperienceRepository experienceRepository;

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
}
