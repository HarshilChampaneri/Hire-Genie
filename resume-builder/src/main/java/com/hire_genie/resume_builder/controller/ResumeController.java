package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.resume.request.ResumeRequest;
import com.hire_genie.resume_builder.service.DynamicResumeGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class ResumeController {

    private final DynamicResumeGeneratorService generator;

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generateResume() {

        ResumeRequest resumeRequest = generator.resumeContentAdder();

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            generator.generateResumeToStream(
                    outputStream,
                    resumeRequest.profile() != null ? resumeRequest.profile() : null,
                    resumeRequest.summary() != null ? resumeRequest.summary() : null,
                    resumeRequest.experiences() != null ? resumeRequest.experiences() : null,
                    resumeRequest.projects() != null ? resumeRequest.projects() : null,
                    resumeRequest.skills() != null ? resumeRequest.skills() : null,
                    resumeRequest.educations() != null ? resumeRequest.educations() : null,
                    resumeRequest.certificates() != null ? resumeRequest.certificates() : null,
                    resumeRequest.others() != null ? resumeRequest.others() : null
            );

            byte[] pdfBytes = outputStream.toByteArray();

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

}
