package com.hire_genie.employee_recommendation_engine.service;

import com.hire_genie.employee_recommendation_engine.dtoMappings.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeRecommendationService {

    private final VectorStore vectorStore;

    public List<ProfileResponse> recommendEmployee(String jobDescription) {

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

    private ProfileResponse mapToEmployeeProfile(Document document) {

        var metadata = document.getMetadata();

        List<String> urlList = (List<String>) metadata.get("urls");

        return ProfileResponse.builder()
                .profileId(((Number) metadata.get("profileId")).longValue())
                .email((String) metadata.get("email"))
                .fullName((String) metadata.get("fullName"))
                .profession((String) metadata.get("profession"))
                .urls(urlList != null ? new HashSet<>(urlList) : Set.of())
                .build();
    }

}
