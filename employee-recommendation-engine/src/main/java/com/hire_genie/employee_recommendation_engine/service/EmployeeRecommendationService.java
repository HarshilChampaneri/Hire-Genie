package com.hire_genie.employee_recommendation_engine.service;

import com.hire_genie.employee_recommendation_engine.config.RetrievalAugmentedGenerationConfig;
import com.hire_genie.employee_recommendation_engine.dtoMappings.ResumeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeRecommendationService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final RetrievalAugmentedGenerationConfig ragConfig;

    public List<ResumeRequest> recommendEmployee(String jobDescription) {

        // TODO: Candidates will be recommended from here:)

        return null;
    }

}
