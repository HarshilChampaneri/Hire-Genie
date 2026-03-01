package com.harshil_infotech.hire_genie.controller;

import com.harshil_infotech.hire_genie.dto.education.request.EducationRequest;
import com.harshil_infotech.hire_genie.dto.education.request.EducationRequestList;
import com.harshil_infotech.hire_genie.dto.education.response.EducationResponse;
import com.harshil_infotech.hire_genie.dto.education.response.EducationResponseList;
import com.harshil_infotech.hire_genie.service.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping("/educations")
    public ResponseEntity<List<EducationResponse>> addEducation(
            @RequestBody @Valid EducationRequestList educationRequestList
    ) {
        return ResponseEntity.ok(educationService.addEducations(educationRequestList));
    }

    @GetMapping("/educations")
    public ResponseEntity<EducationResponseList> getAllEducations() throws Exception {
        return ResponseEntity.ok(educationService.getAllEducations());
    }

    @GetMapping("/educations/{educationId}")
    public ResponseEntity<EducationResponse> getEducationById(@PathVariable Long educationId) {
        return ResponseEntity.ok(educationService.getEducationById(educationId));
    }

    @PutMapping("/educations/{educationId}")
    public ResponseEntity<EducationResponse> updateEducation(
            @PathVariable Long educationId,
            @RequestBody @Valid EducationRequest educationRequest
    ) {
        return ResponseEntity.ok(educationService.updateEducation(educationId, educationRequest));
    }

    @DeleteMapping("/educations/{educationId}")
    public ResponseEntity<String> deleteEducation(
            @PathVariable Long educationId
    ) {
        return ResponseEntity.ok(educationService.deleteEducation(educationId));
    }

}
