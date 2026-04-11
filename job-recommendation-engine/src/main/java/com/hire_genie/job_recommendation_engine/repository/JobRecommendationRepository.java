package com.hire_genie.job_recommendation_engine.repository;

import com.hire_genie.job_recommendation_engine.model.JobVectorMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRecommendationRepository extends JpaRepository<JobVectorMapping, Long> {
}
