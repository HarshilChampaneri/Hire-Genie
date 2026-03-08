package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    Optional<Education> findByEducationIdAndUserEmail(Long educationId, String userEmail);

    @Query("""
            SELECT e FROM Education e
            WHERE e.userEmail = :userEmail AND e.isEducationDeleted = false
            """)
    List<Education> findActiveEducations(@Param("userEmail") String userEmail);

}
