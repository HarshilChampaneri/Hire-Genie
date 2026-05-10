package com.hire_genie.job_service.service.impl;

import com.hire_genie.job_service.dto.candidate.ProfileResponse;
import com.hire_genie.job_service.dto.jobApplication.JobApplicationRequest;
import com.hire_genie.job_service.model.Job;
import com.hire_genie.job_service.model.JobApplication;
import com.hire_genie.job_service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendJobApplicationToEmail(ProfileResponse profileResponse, Job job) {
        try {
            String htmlContent = buildEmailContent(profileResponse, job);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, true, StandardCharsets.UTF_8.name()
            );

            helper.setFrom("trialfornumber1@gmail.com", "TalentBridge");
            helper.setTo(profileResponse.email());
            helper.setSubject("✦ Application Received — " + job.getJobTitle() + " at " + job.getCompany().getCompanyName());
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Job application email sent to {}", profileResponse.email());

        } catch (MessagingException | IOException e) {
            log.error("Failed to sent email to {} : {}", profileResponse.email(), e.getMessage(), e);
        }
    }

    @Override
    public void sendJobApplicationAcceptedToEmail(ProfileResponse profileResponse, JobApplication jobApplication, JobApplicationRequest jobApplicationRequest) {
        try {
            String htmlContent = buildEmailContentForAcceptedApplicants(profileResponse, jobApplication, jobApplicationRequest);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, true, StandardCharsets.UTF_8.name()
            );

            helper.setFrom("trialfornumber1@gmail.com", "TalentBridge");
            helper.setTo(profileResponse.email());
            helper.setSubject("✦ Application Accepted — " + jobApplication.getJob().getJobTitle() + " at " + jobApplication.getJob().getCompany().getCompanyName());
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Job application accepted email sent to {}", profileResponse.email());

        } catch (MessagingException | IOException e) {
            log.error("Failed to sent email to {} : {}", profileResponse.email(), e.getMessage(), e);
        }
    }

    @Override
    public void sendJobApplicationRejectedToEmail(ProfileResponse profileResponse, Job job) {
        try {
            String htmlContent = buildEmailContentForRejectedApplicants(profileResponse, job);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, true, StandardCharsets.UTF_8.name()
            );

            helper.setFrom("trialfornumber1@gmail.com", "TalentBridge");
            helper.setTo(profileResponse.email());
            helper.setSubject("✦ Application Rejected — " + job.getJobTitle() + " at " + job.getCompany().getCompanyName());
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Job application rejected email sent to {}", profileResponse.email());

        } catch (MessagingException | IOException e) {
            log.error("Failed to sent email to {} : {}", profileResponse.email(), e.getMessage(), e);
        }
    }

    private String buildEmailContentForAcceptedApplicants(ProfileResponse profileResponse, JobApplication jobApplication, JobApplicationRequest jobApplicationRequest) throws IOException {

        ClassPathResource resource = new ClassPathResource("templates/email/job-application-accepted.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        String firstName = profileResponse.fullName().split(" ")[0];
        String acceptedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));

        // Parse from plain String instead of java.sql.Date / java.sql.Time
        String formattedDate = LocalDate.parse(jobApplicationRequest.interviewDate())
                .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));

        String rawTime = jobApplicationRequest.interviewTime();
        if (rawTime.matches("\\d{2}:\\d{2}")) {
            rawTime = rawTime + ":00";
        }
        String formattedTime = LocalTime.parse(rawTime)
                .format(DateTimeFormatter.ofPattern("hh:mm a"));

        String additionalDetails = (jobApplicationRequest.additionalDetails() != null
                && !jobApplicationRequest.additionalDetails().isBlank())
                ? jobApplicationRequest.additionalDetails()
                : "No additional details provided.";

        return template
                .replace("{{FULL_NAME}}", profileResponse.fullName())
                .replace("{{FIRST_NAME}}", firstName)
                .replace("{{JOB_TITLE}}", jobApplication.getJob().getJobTitle())
                .replace("{{COMPANY_NAME}}", jobApplication.getJob().getCompany().getCompanyName())
                .replace("{{ACCEPTED_DATE}}", acceptedDate)
                .replace("{{INTERVIEW_DATE}}", formattedDate)
                .replace("{{INTERVIEW_TIME}}", formattedTime)
                .replace("{{VENUE}}", jobApplicationRequest.venue())
                .replace("{{ADDITIONAL_DETAILS}}", additionalDetails);
    }

    private String buildEmailContentForRejectedApplicants(ProfileResponse profileResponse, Job job) throws IOException {

        ClassPathResource resource = new ClassPathResource("templates/email/job-application-rejected.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        String firstName = profileResponse.fullName().split(" ")[0];
        String rejectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MM, yyyy"));

        return template
                .replace("{{FULL_NAME}}", profileResponse.fullName())
                .replace("{{FIRST_NAME}}", firstName)
                .replace("{{JOB_TITLE}}", job.getJobTitle())
                .replace("{{COMPANY_NAME}}", job.getCompany().getCompanyName())
                .replace("{{REJECTED_DATE}}", rejectedDate);

    }

    private String buildEmailContent(ProfileResponse profileResponse, Job job) throws IOException {

        ClassPathResource resource = new ClassPathResource("templates/email/job-application.html");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        String firstName = profileResponse.fullName().split(" ")[0];
        String appliedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MM, yyyy"));

        return template
                .replace("{{FULL_NAME}}", profileResponse.fullName())
                .replace("{{FIRST_NAME}}", firstName)
                .replace("{{JOB_TITLE}}", job.getJobTitle())
                .replace("{{COMPANY_NAME}}", job.getCompany().getCompanyName())
                .replace("{{APPLIED_DATE}}", appliedDate);

    }

}
