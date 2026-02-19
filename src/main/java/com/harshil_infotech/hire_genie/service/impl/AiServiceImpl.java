package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import static com.harshil_infotech.hire_genie.util.prompts.SystemPrompt.systemPrompt;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    @Override
    public String provideSkillSummary(String text) {

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(text)
                .call()
                .content();

    }
}
