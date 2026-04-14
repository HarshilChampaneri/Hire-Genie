package com.hire_genie.job_service.repository;

import com.hire_genie.job_service.model.Company;
import com.hire_genie.job_service.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
            SELECT j FROM Job j
            WHERE j.isJobDeleted = false
            """)
    Page<Job> findAllActiveJobs(Pageable pageable);

    @Query("""
            SELECT j FROM Job j
            WHERE j.isJobDeleted = false AND j.company = :company
            """)
    Page<Job> findAllActiveJobsByCompany(@Param("company")Company company, Pageable pageable);

    @Query("""
            SELECT j FROM Job j
            WHERE j.isJobDeleted = false AND j.jobId = :jobId AND j.userEmail = :userEmail
            """)
    Optional<Job> findByJobId(@Param("jobId") Long jobId, @Param("userEmail") String userEmail);

    @Query("""
            SELECT j FROM Job j
            WHERE j.isJobDeleted = false AND j.jobId = :jobId
            """)
    Optional<Job> findByJobIdIgnoringUserEmail(@Param("jobId") Long jobId);
}
