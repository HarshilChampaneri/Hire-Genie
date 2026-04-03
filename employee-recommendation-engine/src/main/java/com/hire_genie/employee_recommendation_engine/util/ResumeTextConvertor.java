package com.hire_genie.employee_recommendation_engine.util;

import com.hire_genie.employee_recommendation_engine.dtoMappings.ResumeRequest;
import org.springframework.stereotype.Component;

@Component
public class ResumeTextConvertor {

    public String convert(ResumeRequest resumeRequest) {

        StringBuilder sb = new StringBuilder();

        if (resumeRequest.profile() != null) {
            sb.append("Profile ID: ").append(resumeRequest.profile().profileId()).append("\n");
            sb.append("Name: ").append(resumeRequest.profile().fullName()).append("\n");
            if (resumeRequest.profile().mobileNo() != null)
                sb.append("Mobile No.: ").append(resumeRequest.profile().mobileNo()).append("\n");
            sb.append("Email: ").append(resumeRequest.profile().email()).append("\n");
            sb.append("Profession: ").append(resumeRequest.profile().profession()).append("\n");
            sb.append("URLs: ").append(resumeRequest.profile().urls()).append("\n");
        }

        if (resumeRequest.summary() != null) {
            sb.append("Summary: ").append(resumeRequest.summary().profileSummary()).append("\n");
        }

        if (resumeRequest.experiences() != null) {
            resumeRequest.experiences().experiences().forEach(exp -> {
                sb.append("Experience: ").append(exp.position())
                        .append(" at ").append(exp.companyName())
                        .append(" from ").append(exp.startDate())
                        .append(" till ").append(exp.isWorkingInCompany() ? " Present " : exp.endDate())
                        .append("\n");
                if (exp.description() != null)
                    exp.description().forEach(d -> sb.append(" - ").append(d).append("\n"));
            });
        }

        if (resumeRequest.projects() != null) {
            resumeRequest.projects().projects().forEach(proj -> {
                sb.append("Project: ").append(proj.projectName()).append("\n");
                if (proj.projectTechStacks() != null)
                    sb.append(" Tech Stack: ").append(String.join(", ", proj.projectTechStacks())).append("\n");
                sb.append("Start Date: ")
                        .append(proj.projectStartDate())
                        .append(" - End Date: ")
                        .append(proj.isProjectInProgress()
                                ? " Present "
                                : proj.projectEndDate())
                        .append("\n");
                if (proj.projectDescription() != null)
                    proj.projectDescription().forEach(d -> sb.append("  - ").append(d).append("\n"));
            });
        }

        if (resumeRequest.skills() != null && resumeRequest.skills().technicalSkills() != null) {

            resumeRequest.skills().technicalSkills().forEach((category, skillList) ->
                    sb.append("Skills - ").append(category).append(": ")
                            .append(String.join(", ", skillList)).append("\n")
            );

        }

        if (resumeRequest.educations() != null) {
            resumeRequest.educations().educations().forEach(edu ->
                    sb.append("Education: ").append(edu.educationTitle())
                            .append(", ")
                            .append(edu.fieldOfStudy())
                            .append("From: ")
                            .append(edu.startDate())
                            .append(" - Till: ")
                            .append(edu.isEducationInProgress() ? " Present " : edu.endDate())
                            .append("\n")
            );
        }

        if (resumeRequest.certificates() != null) {
            resumeRequest.certificates().certificates().forEach(cert ->
                    sb.append("Certificate: ")
                            .append(cert.certificateTitle())
                            .append("Certificate URL: ")
                            .append(cert.certificateUrl())
            );
        }

        if (resumeRequest.others() != null) {
            resumeRequest.others().description().forEach(d -> {
                sb.append("Description: ").append(d).append("\n");
            });
        }

        return sb.toString();
    }
}
