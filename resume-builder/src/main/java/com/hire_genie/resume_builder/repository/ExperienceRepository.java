package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    Optional<Experience> findByExperienceIdAndUserEmail(Long experienceId, String userEmail);

    @Query("""
            SELECT e FROM Experience e
            WHERE e.userEmail = :userEmail AND e.isExperienceDeleted = false
            """)
    List<Experience> findActiveExperiences(@Param("userEmail") String userEmail);

}
