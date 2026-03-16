package com.hire_genie.job_service.repository;

import com.hire_genie.job_service.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("""
            SELECT c FROM Company c
            WHERE c.isCompanyDeleted = false
            """)
    Page<Company> findAllActiveCompanies(Pageable pageable);

    @Query("""
            SELECT c FROM Company c
            WHERE c.isCompanyDeleted = false AND c.companyId = :companyId AND c.userEmail = :userEmail
            """)
    Company findCompanyById(
            @Param("companyId") Long companyId,
            @Param("userEmail") String userEmail
    );
}
