package com.hire_genie.job_service.repository;

import com.hire_genie.job_service.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("""
            SELECT c FROM Company c
            WHERE c.isCompanyDeleted = false
            """)
    List<Company> findAllActiveCompanies();

}
