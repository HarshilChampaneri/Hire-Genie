package com.hire_genie.employee_recommendation_engine.service;

import com.hire_genie.employee_recommendation_engine.dtoMappings.EmployeeProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeRecommendationService {

    private final VectorStore vectorStore;

    public List<EmployeeProfile> recommendEmployee(String jobDescription) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(jobDescription)
                .topK(5)
                .similarityThreshold(0.65)
                .build();

        List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);

        return similarDocuments.stream()
                .map(this::mapToEmployeeProfile)
                .toList();
    }

    private EmployeeProfile mapToEmployeeProfile(Document document) {

        var metadata = document.getMetadata();

        return EmployeeProfile.builder()
                .email((String) metadata.get("email"))
                .fullName((String) metadata.get("fullName"))
                .profession((String) metadata.get("profession"))
                .build();
    }

}
