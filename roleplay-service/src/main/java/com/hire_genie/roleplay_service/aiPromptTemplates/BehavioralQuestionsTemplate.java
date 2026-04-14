package com.hire_genie.roleplay_service.aiPromptTemplates;

import org.springframework.stereotype.Component;

@Component
public class BehavioralQuestionsTemplate {

    public static final String BEHAVIORAL_QUESTIONS_TEMPLATE = """
            You are a behavioral interview expert. Based on the job description provided below, generate exactly 5 behavioral interview questions that are most likely to be asked for this role.
            
            Respond ONLY with a valid format as mentioned below — no explanation, no markdown, no code fences.
            {format}
            
            Rules:
            - Return exactly 5 objects in the array.
            - Questions must be specific and relevant to the job description.
            - The "solution" field should be thorough and reflect what a senior-level candidate would answer.
            - DO NOT include any text outside the format.
            - DO NOT wrap the response in code blocks or backticks.
            
            Job Description:
            {jobDescription}
            """;

}
