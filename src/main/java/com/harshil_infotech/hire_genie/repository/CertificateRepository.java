package com.harshil_infotech.hire_genie.repository;

import com.harshil_infotech.hire_genie.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
