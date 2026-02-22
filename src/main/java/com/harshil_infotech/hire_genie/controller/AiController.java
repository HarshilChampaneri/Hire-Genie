package com.harshil_infotech.hire_genie.controller;

import com.harshil_infotech.hire_genie.dto.response.SkillSummaryResponse;
import com.harshil_infotech.hire_genie.service.AiService;
import com.harshil_infotech.hire_genie.prompts.user_prompts.DefaultUserSkillPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AiController {

    private final AiService aiService;

    @GetMapping("/provide/skills/summary")
    public ResponseEntity<SkillSummaryResponse> getSkillSummary(
            @RequestParam(
                    name = "text",
                    defaultValue = DefaultUserSkillPrompt.defaultUserSkillPrompt,
                    required = false
            ) String text
    ) {
        return ResponseEntity.ok(aiService.provideSkillSummary(text));
    }

}
