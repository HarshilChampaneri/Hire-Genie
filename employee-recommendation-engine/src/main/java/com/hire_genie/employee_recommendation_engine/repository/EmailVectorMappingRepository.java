package com.hire_genie.employee_recommendation_engine.repository;

import com.hire_genie.employee_recommendation_engine.vectorMappings.EmailVectorMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVectorMappingRepository extends JpaRepository<EmailVectorMapping, String> {
}
