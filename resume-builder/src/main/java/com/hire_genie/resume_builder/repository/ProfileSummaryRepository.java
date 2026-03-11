package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Profile;
import com.hire_genie.resume_builder.model.ProfileSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileSummaryRepository extends JpaRepository<ProfileSummary, Long> {

    @Query("""
            SELECT p FROM ProfileSummary p
            WHERE p.userEmail = :userEmail AND p.isProfileSummaryDeleted = false
            """)
    ProfileSummary findExistingProfileSummary(@Param("userEmail") String userEmail);

}
