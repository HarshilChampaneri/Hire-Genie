package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findByCertificateIdAndUserEmail(Long certificateId, String userEmail);

    @Query("""
            SELECT c FROM Certificate c
            WHERE c.userEmail = :userEmail AND c.isCertificateDeleted = false
            """)
    List<Certificate> findActiveCertificates(@Param("userEmail") String userEmail);

}
