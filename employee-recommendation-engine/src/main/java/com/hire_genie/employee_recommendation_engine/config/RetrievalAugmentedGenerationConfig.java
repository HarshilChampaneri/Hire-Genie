package com.hire_genie.employee_recommendation_engine.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class VectorStoreConfig {

    @Bean
    public List<Document> vectorStore(VectorStore vectorStore) {
        return vectorStore.similaritySearch(SearchRequest.builder()
                        .topK(7)
                        .similarityThreshold(0.65)
                .build()
        );
    }

}
