package com.hire_genie.job_service.repository;

import com.hire_genie.job_service.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    @Query("""
            SELECT ja FROM JobApplication ja
            WHERE ja.job.jobId = :jobId AND ja.candidateEmail = :candidateEmail
            """)
    Optional<JobApplication> findByJobIdAndCandidateEmail(@Param("jobId") Long jobId, @Param("candidateEmail") String candidateEmail);

    @Query("""
            SELECT ja FROM JobApplication ja
            WHERE ja.id = :jobApplicationId AND ja.isJobApplicationDeleted = false
            """)
    Optional<JobApplication> findByJobApplicationId(@Param("jobApplicationId") Long jobApplicationId);

    @Query("""
        SELECT ja FROM JobApplication ja
        WHERE ja.recruiterEmail = :userEmail AND ja.isJobApplicationDeleted = false
        """)
    Page<JobApplication> findAllMyPendingJobApplications(@Param("userEmail") String userEmail, Pageable pageable);
}
