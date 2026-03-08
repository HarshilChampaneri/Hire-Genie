package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Other;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtherRepository extends JpaRepository<Other, Long> {

    Optional<Other> findByOtherIdAndUserEmail(Long otherId, String userEmail);

}
