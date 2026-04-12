package com.hire_genie.job_recommendation_engine.service;

import com.hire_genie.job_recommendation_engine.dtoMappings.employeeDTO.ResumeRequest;
import com.hire_genie.job_recommendation_engine.dtoMappings.jobDTO.JobResponse;
import com.hire_genie.job_recommendation_engine.enums.JobType;
import com.hire_genie.job_recommendation_engine.enums.WorkMode;
import com.hire_genie.job_recommendation_engine.exception.InvalidAccessException;
import com.hire_genie.job_recommendation_engine.feignClient.UserProfileFeignClient;
import com.hire_genie.job_recommendation_engine.security.util.LoggedInUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobRecommendationService {

    private final LoggedInUser loggedInUser;
    private final UserProfileFeignClient userProfileFeignClient;
    private final VectorStore vectorStore;

    public List<JobResponse> recommendJobs(HttpServletRequest request) {

        String secret = request.getHeader("X-Internal-Secret");
        String email = request.getHeader("X-User-Email");
        String roles = request.getHeader("X-User-Roles");

        if (!loggedInUser.isEmployee()) {
            throw new InvalidAccessException("You are Unauthorized to access this Service");
        }

        ResumeRequest userProfile = userProfileFeignClient.fetchUserProfile(secret, email, roles);

        String profile = userProfileToString(userProfile);

        SearchRequest searchRequest = SearchRequest.builder()
                .query(profile)
                .topK(5)
                .similarityThreshold(0.65)
                .build();

        List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);

        return similarDocuments.stream()
                .map(this::mapToJobResponse)
                .toList();
    }

    private JobResponse mapToJobResponse(Document document) {

        var metaData = document.getMetadata();

        return JobResponse.builder()
                .jobId(metaData.get("jobId") != null ? Long.valueOf(metaData.get("jobId").toString()) : null)
                .jobTitle((String) metaData.get("jobTitle"))
                .jobDescription((String) metaData.get("jobDescription"))
                .jobType(JobType.valueOf(metaData.get("jobType").toString()))
                .workMode(WorkMode.valueOf(metaData.get("workMode").toString()))
                .location((String) metaData.get("location"))
                .minSalary(metaData.get("minSalary") != null ? new BigDecimal(metaData.get("minSalary").toString()) : null)
                .maxSalary(metaData.get("maxSalary") != null ? new BigDecimal(metaData.get("maxSalary").toString()) : null)
                .currency((String) metaData.get("currency"))
                .vacancies((Integer) metaData.get("vacancies"))
                .build();

    }

    private String userProfileToString(ResumeRequest userProfile) {
        StringBuilder sb = new StringBuilder();

        if (userProfile.profile() != null) {
            sb.append("Profile ID: ").append(userProfile.profile().profileId()).append("\n");
            sb.append("Name: ").append(userProfile.profile().fullName()).append("\n");
            if (userProfile.profile().mobileNo() != null)
                sb.append("Mobile No.: ").append(userProfile.profile().mobileNo()).append("\n");
            sb.append("Email: ").append(userProfile.profile().email()).append("\n");
            sb.append("Profession: ").append(userProfile.profile().profession()).append("\n");
            sb.append("URLs: ").append(userProfile.profile().urls()).append("\n");
        }

        if (userProfile.summary() != null) {
            sb.append("Summary: ").append(userProfile.summary().profileSummary()).append("\n");
        }

        if (userProfile.experiences() != null) {
            userProfile.experiences().experiences().forEach(exp -> {
                sb.append("Experience: ").append(exp.position())
                        .append(" at ").append(exp.companyName())
                        .append(" from ").append(exp.startDate())
                        .append(" till ").append(exp.isWorkingInCompany() ? " Present " : exp.endDate())
                        .append("\n");
                if (exp.description() != null)
                    exp.description().forEach(d -> sb.append(" - ").append(d).append("\n"));
            });
        }

        if (userProfile.projects() != null) {
            userProfile.projects().projects().forEach(proj -> {
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

        if (userProfile.skills() != null && userProfile.skills().technicalSkills() != null) {

            userProfile.skills().technicalSkills().forEach((category, skillList) ->
                    sb.append("Skills - ").append(category).append(": ")
                            .append(String.join(", ", skillList)).append("\n")
            );

        }

        if (userProfile.educations() != null) {
            userProfile.educations().educations().forEach(edu ->
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

        if (userProfile.certificates() != null) {
            userProfile.certificates().certificates().forEach(cert ->
                    sb.append("Certificate: ")
                            .append(cert.certificateTitle())
                            .append("Certificate URL: ")
                            .append(cert.certificateUrl())
            );
        }

        if (userProfile.others() != null) {
            userProfile.others().description().forEach(d -> {
                sb.append("Description: ").append(d).append("\n");
            });
        }

        return sb.toString();
    }
}
