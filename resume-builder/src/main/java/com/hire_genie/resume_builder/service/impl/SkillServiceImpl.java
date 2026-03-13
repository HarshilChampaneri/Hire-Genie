package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.skill_summary.response.SkillSummaryResponse;
import com.hire_genie.resume_builder.mapper.SkillMapper;
import com.hire_genie.resume_builder.model.Skill;
import com.hire_genie.resume_builder.repository.SkillRepository;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final LoggedInUser loggedInUser;

    @Override
    public SkillSummaryResponse getSkills() throws Exception {
        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Skill skill = skillRepository.findActiveSkill(userEmail);
        if (skill == null) {
            throw new Exception("No Skill found.");
        }

        return skillMapper.toSkillResponseFromSkill(skill);
    }

}
