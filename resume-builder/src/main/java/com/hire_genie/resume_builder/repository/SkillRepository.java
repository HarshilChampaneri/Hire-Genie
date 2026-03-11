package com.hire_genie.resume_builder.repository;

import com.hire_genie.resume_builder.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("""
            SELECT s FROM Skill s
            WHERE s.userEmail = :userEmail AND s.isSkillDeleted = false
            """)
    Skill findActiveSkill(@Param("userEmail") String userEmail);

}
