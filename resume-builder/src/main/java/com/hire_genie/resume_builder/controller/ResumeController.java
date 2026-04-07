package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.resume.request.ResumeRequest;
import com.hire_genie.resume_builder.feignClient.EmployeeRecommendationEngineClient;
import com.hire_genie.resume_builder.service.DynamicResumeGeneratorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class ResumeController {

    private final DynamicResumeGeneratorService generator;
    private final EmployeeRecommendationEngineClient employeeRecommendationEngineClient;

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generateResume(HttpServletRequest request) {

        ResumeRequest resumeRequest = generator.resumeContentAdder();

        String secret = request.getHeader("X-Internal-Secret");
        String email = request.getHeader("X-User-Email");
        String roles = request.getHeader("X-User-Roles");

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            generator.generateResumeToStream(
                    outputStream,
                    resumeRequest.profile(),
                    resumeRequest.summary(),
                    resumeRequest.experiences(),
                    resumeRequest.projects(),
                    resumeRequest.skills(),
                    resumeRequest.educations(),
                    resumeRequest.certificates(),
                    resumeRequest.others()
            );

            byte[] pdfBytes = outputStream.toByteArray();

            pushToRecommendationEngine(resumeRequest, secret, email, roles);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Resume.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadResume() throws IOException {

        // Fetch all resume content
        ResumeRequest resumeRequest = generator.resumeContentAdder();

        // Write PDF into a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        generator.generateResumeToStream(
                baos,
                resumeRequest.profile(),
                resumeRequest.summary(),
                resumeRequest.experiences(),
                resumeRequest.projects(),
                resumeRequest.skills(),
                resumeRequest.educations(),
                resumeRequest.certificates(),
                resumeRequest.others()
        );

        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // "attachment" triggers download, "inline" opens in browser
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("resume.pdf")
                        .build()
        );
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/get-my-profile")
    public ResponseEntity<ResumeRequest> getMyProfile() {
        return ResponseEntity.ok(generator.resumeContentAdder());
    }

    @Async
    protected void pushToRecommendationEngine(ResumeRequest resumeRequest, String secret, String email, String roles) {
        try {
            // Manually passing headers to the Feign Client via the method arguments
            employeeRecommendationEngineClient.storeResume(secret, email, roles, resumeRequest);
        } catch (Exception e) {
            log.warn("Failed to push resume to recommendation engine: {}", e.getMessage());
        }
    }
}
