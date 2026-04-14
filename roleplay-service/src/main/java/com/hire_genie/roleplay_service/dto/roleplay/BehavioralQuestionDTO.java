package com.hire_genie.roleplay_service.dto.roleplay;

import lombok.Builder;

@Builder
public record BehavioralQuestionDTO(
        String question,
        String intentionToAsk,
        String solution
) {
}
