package com.hire_genie.job_service.repository;

import com.hire_genie.job_service.model.Company;
import com.hire_genie.job_service.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
            SELECT j FROM Job j
            WHERE j.isJobDeleted = false
            """)
    List<Job> findAllActiveJobs();

    @Query("""
            SELECT j FROM Job j
            WHERE j.isJobDeleted = false AND j.company = :company
            """)
    List<Job> findAllActiveJobsByCompany(@Param("company")Company company);

    @Query("""
            SELECT j FROM Job j
            WHERE j.isJobDeleted = false AND j.jobId = :jobId AND j.userEmail = :userEmail
            """)
    Optional<Job> findByJobId(@Param("jobId") Long jobId, @Param("userEmail") String userEmail);

}
