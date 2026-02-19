package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.dto.response.SkillSummaryResponse;
import com.harshil_infotech.hire_genie.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import static com.harshil_infotech.hire_genie.prompts.system_prompts.SkillSystemPrompt.skillSystemPrompt;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

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
            return new SkillSummaryResponse(
                    null,
                    null,
                    null,
                    null
            );
        }

    }

}
