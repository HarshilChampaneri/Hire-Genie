package com.harshil_infotech.hire_genie.controller;

import com.harshil_infotech.hire_genie.service.AiService;
import com.harshil_infotech.hire_genie.util.prompts.DefaultUserPrompt;
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
    public ResponseEntity<String> getSkillSummary(
            @RequestParam(
                    name = "text",
                    defaultValue = DefaultUserPrompt.defaultUserPrompt,
                    required = false
            ) String text
    ) {
        return ResponseEntity.ok(aiService.provideSkillSummary(text));
    }

}
