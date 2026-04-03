package com.hire_genie.employee_recommendation_engine.service;

import com.hire_genie.employee_recommendation_engine.dtoMappings.ResumeRequest;
import com.hire_genie.employee_recommendation_engine.repository.EmailVectorMappingRepository;
import com.hire_genie.employee_recommendation_engine.util.ResumeTextConvertor;
import com.hire_genie.employee_recommendation_engine.vectorMappings.EmailVectorMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeVectorStoreService {

    private final VectorStore vectorStore;
    private final EmailVectorMappingRepository mappingRepository;
    private final ResumeTextConvertor resumeTextConvertor;

    @Transactional
    public void upsertResume(ResumeRequest resumeRequest) {

        if (resumeRequest.projects() == null || resumeRequest.profile().email() == null) {
            log.warn("Skipping upsert: profile or email is null");
            return;
        }

        String email = resumeRequest.profile().email();

        mappingRepository.findById(email).ifPresent(mapping -> {
            vectorStore.delete(List.of(mapping.getVectorDocumentId()));
            log.info("Deleted old vector document for email: {}", email);
        });

        String resumeText = resumeTextConvertor.convert(resumeRequest);
        Map<String, Object> metaData = Map.of(
                "email", email,
                "fullName", resumeRequest.profile().fullName(),
                "profession", resumeRequest.profile().profession() != null ? resumeRequest.profile().profession() : ""
        );

        Document document = new Document(resumeText, metaData);
        vectorStore.add(List.of(document));

        mappingRepository.save(
                EmailVectorMapping.builder()
                        .email(email)
                        .vectorDocumentId(document.getId())
                        .build()
        );

        log.info("Upserted resume vector for email: {}", email);

    }

}
