package com.hire_genie.job_service.repository;

import com.hire_genie.job_service.model.JobCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobCandidateRepository extends JpaRepository<JobCandidate, Long> {

    @Query("""
            SELECT jc FROM JobCandidate jc
            WHERE jc.jobId = :jobId AND jc.candidateEmail = :candidateEmail
            """)
    Optional<JobCandidate> findByJobIdAndCandidateEmail(@Param("jobId") Long jobId, @Param("candidateEmail") String candidateEmail);

}
