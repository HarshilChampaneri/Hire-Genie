package com.harshil_infotech.hire_genie.repository;

import com.harshil_infotech.hire_genie.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
}
