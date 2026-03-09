package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("""
            SELECT p FROM Profile p
            WHERE p.userEmail = :userEmail AND p.isProfileDeleted = false
            """)
    Profile findExistingProfile(@Param("userEmail") String userEmail);

}
