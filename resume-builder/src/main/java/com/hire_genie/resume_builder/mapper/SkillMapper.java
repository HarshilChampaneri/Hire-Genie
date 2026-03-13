package com.hire_genie.resume_builder.mapper;

import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;
import com.hire_genie.resume_builder.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillSummaryResponse toSkillResponseFromSkill(Skill skill) {

        return SkillSummaryResponse.builder()
                .technicalSkills(skill.getSkills())
                .build();

    }

}
