package com.hire_genie.resume_builder.controller;

import com.hire_genie.resume_builder.dto.other.request.OtherRequest;
import com.hire_genie.resume_builder.dto.other.response.OtherResponse;
import com.hire_genie.resume_builder.service.OtherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'RECRUITER', 'ADMIN')")
public class OtherController {

    private final OtherService otherService;

    @PostMapping("/others")
    public ResponseEntity<OtherResponse> addOther (@RequestBody @Valid OtherRequest otherRequest) {

        return ResponseEntity.ok(otherService.addOthers(otherRequest));

    }

    @GetMapping("/others")
    public ResponseEntity<OtherResponse> getOther() throws Exception {

        return ResponseEntity.ok(otherService.getOther());

    }

    @PutMapping("/others")
    public ResponseEntity<OtherResponse> updateOther(
            @RequestBody @Valid OtherRequest otherRequest
    ) throws Exception {

        return ResponseEntity.ok(otherService.updateOther(otherRequest));

    }

    @DeleteMapping("/others")
    public ResponseEntity<String> deleteOther () {

        return ResponseEntity.ok(otherService.deleteOther());

    }

}
