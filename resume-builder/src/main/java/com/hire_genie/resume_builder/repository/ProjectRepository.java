package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectIdAndUserEmail(Long projectId, String userEmail);

    @Query("""
            SELECT p FROM Project p
            WHERE p.userEmail = :userEmail AND p.isProjectDeleted = false
            """)
    List<Project> findActiveProjects(@Param("userEmail") String userEmail);

}
