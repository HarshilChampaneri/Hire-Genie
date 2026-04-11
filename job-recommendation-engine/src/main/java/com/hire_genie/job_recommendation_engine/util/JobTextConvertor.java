package com.hire_genie.job_recommendation_engine.util;

import com.hire_genie.job_recommendation_engine.dtoMappings.jobDTO.JobResponse;
import org.springframework.stereotype.Component;

@Component
public class JobTextConvertor {

    public String convert(JobResponse jobResponse) {

        StringBuilder sb = new StringBuilder();

        if (jobResponse != null) {
            if (!jobResponse.jobTitle().isEmpty()) {
                sb.append("Job Title: ").append(jobResponse.jobTitle());
            }
            if (!jobResponse.jobDescription().isEmpty()) {
                sb.append("Job Description: ").append(jobResponse.jobDescription());
            }
            if (jobResponse.jobType() != null) {
                sb.append("Job Type: ").append(jobResponse.jobType());
            }
            if (jobResponse.workMode() != null) {
                sb.append("Work Mode: ").append(jobResponse.workMode());
            }
            if (!jobResponse.location().isEmpty()) {
                sb.append("Location: ").append(jobResponse.location());
            }
            if (jobResponse.minSalary() != null) {
                sb.append("Min Salary: ").append(jobResponse.minSalary());
            }
            if (jobResponse.maxSalary() != null) {
                sb.append("Max Salary: ").append(jobResponse.maxSalary());
            }
            if (!jobResponse.currency().isEmpty()) {
                sb.append("Currency: ").append(jobResponse.currency());
            }
            if (jobResponse.vacancies() != null) {
                sb.append("Vacancies: ").append(jobResponse.vacancies());
            }
        }

        return sb.toString();
    }

}
