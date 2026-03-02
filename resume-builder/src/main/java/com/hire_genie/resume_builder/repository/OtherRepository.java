package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Other;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherRepository extends JpaRepository<Other, Long> {
}
