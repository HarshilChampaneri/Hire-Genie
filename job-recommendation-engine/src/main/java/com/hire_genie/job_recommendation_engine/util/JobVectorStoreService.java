package com.hire_genie.job_recommendation_engine.util;

import com.hire_genie.job_recommendation_engine.dtoMappings.jobDTO.JobResponse;
import com.hire_genie.job_recommendation_engine.model.JobVectorMapping;
import com.hire_genie.job_recommendation_engine.repository.JobRecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobVectorStoreService {

    private final VectorStore vectorStore;
    private final JobRecommendationRepository jobRecommendationRepository;
    private final JobTextConvertor jobTextConvertor;

    @Transactional
    public void upsertJobProfile(JobResponse jobResponse) {

        if (jobResponse.jobId() == null) {
            log.warn("Skipping upsert: JobID is null");
            return;
        }

        Long jobId = jobResponse.jobId();

        jobRecommendationRepository.findById(jobId).ifPresent(mapping -> {
            vectorStore.delete(List.of(mapping.getVectorDocumentId()));
            log.info("Deleted old vector document for JobID: {}", jobId);
        });

        String jobText = jobTextConvertor.convert(jobResponse);
        Document document = getDocument(jobResponse, jobId, jobText);
        vectorStore.add(List.of(document));

        jobRecommendationRepository.save(
                JobVectorMapping.builder()
                        .jobId(jobId)
                        .vectorDocumentId(document.getId())
                        .build()
        );

        log.info("Upserted Job Profile vector for JobID: {}", jobId);

    }

    private static @NonNull Document getDocument(JobResponse jobResponse, Long jobId, String jobText) {
        Map<String, Object> metaData = Map.of(
                "jobId", jobId,
                "jobTitle", jobResponse.jobTitle(),
                "jobDescription", jobResponse.jobDescription(),
                "jobType", jobResponse.jobType(),
                "workMode", jobResponse.workMode(),
                "location", jobResponse.location(),
                "minSalary", jobResponse.minSalary(),
                "maxSalary", jobResponse.maxSalary(),
                "currency", jobResponse.currency(),
                "vacancies", jobResponse.vacancies()
        );

        return new Document(jobText, metaData);
    }
}
