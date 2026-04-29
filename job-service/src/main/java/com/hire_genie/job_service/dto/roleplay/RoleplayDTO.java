package com.hire_genie.job_service.dto.roleplay;

import lombok.Builder;

import java.util.List;

@Builder
public record RoleplayDTO(
        List<TechnicalQuestionDTO> technicalQuestions,
        List<BehavioralQuestionDTO> behavioralQuestions
) {
}
