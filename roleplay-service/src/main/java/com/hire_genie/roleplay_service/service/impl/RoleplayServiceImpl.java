package com.hire_genie.roleplay_service.service.impl;

import com.hire_genie.roleplay_service.dto.roleplay.BehavioralQuestionDTO;
import com.hire_genie.roleplay_service.dto.roleplay.RoleplayDTO;
import com.hire_genie.roleplay_service.dto.roleplay.TechnicalQuestionDTO;
import com.hire_genie.roleplay_service.service.RoleplayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.hire_genie.roleplay_service.aiPromptTemplates.BehavioralQuestionsTemplate.BEHAVIORAL_QUESTIONS_TEMPLATE;
import static com.hire_genie.roleplay_service.aiPromptTemplates.TechnicalQuestionsTemplate.TECHNICAL_QUESTIONS_TEMPLATE;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleplayServiceImpl implements RoleplayService {

    private final ChatClient chatClient;

    @Override
    public RoleplayDTO startRoleplayService(String jobDescription) {

        // Generating Technical Questions:
        BeanOutputConverter<List<TechnicalQuestionDTO>> technicalQuestionsConvertor = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<TechnicalQuestionDTO>>() {}
        );

        Prompt technicalPrompt = PromptTemplate.builder()
                .template(TECHNICAL_QUESTIONS_TEMPLATE)
                .variables(Map.of(
                        "format", technicalQuestionsConvertor.getFormat(),
                        "jobDescription", jobDescription
                ))
                .build()
                .create();

        Generation technicalQuestionsGeneration = Objects.requireNonNull(chatClient
                        .prompt(technicalPrompt)
                        .call()
                        .chatResponse())
                .getResult();

        assert technicalQuestionsGeneration != null;
        String technicalRawJson = technicalQuestionsGeneration.getOutput().getText();
        log.info("Raw AI Response: {}", technicalRawJson);

        assert technicalRawJson != null;
        List<TechnicalQuestionDTO> technicalQuestions = technicalQuestionsConvertor.convert(technicalRawJson);
        log.info("Parsed Questions: {}", technicalQuestions);

        // Generating Behavioral Questions:
        BeanOutputConverter<List<BehavioralQuestionDTO>> behavioralQuestionsConvertor = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<BehavioralQuestionDTO>>() {}
        );

        Prompt behavioralPrompt = PromptTemplate.builder()
                .template(BEHAVIORAL_QUESTIONS_TEMPLATE)
                .variables(Map.of(
                        "format", behavioralQuestionsConvertor.getFormat(),
                        "jobDescription", jobDescription
                ))
                .build()
                .create();

        Generation behavioralQuestionsGeneration = Objects.requireNonNull(chatClient
                        .prompt(behavioralPrompt)
                        .call()
                        .chatResponse())
                .getResult();

        assert behavioralQuestionsGeneration != null;
        String behavioralRawJson = behavioralQuestionsGeneration.getOutput().getText();
        log.info("Raw AI Response: {}", behavioralRawJson);

        assert behavioralRawJson != null;
        List<BehavioralQuestionDTO> behavioralQuestions = behavioralQuestionsConvertor.convert(behavioralRawJson);
        log.info("Parsed Questions: {}", behavioralQuestions);

        return RoleplayDTO.builder()
                .technicalQuestions(technicalQuestions)
                .behavioralQuestions(behavioralQuestions)
                .build();
    }
}
