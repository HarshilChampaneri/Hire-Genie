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
@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_RECRUITER', 'ROLE_ADMIN')")
public class OtherController {

    private final OtherService otherService;

    @PostMapping("/others")
    public ResponseEntity<OtherResponse> addOther (@RequestBody @Valid OtherRequest otherRequest) {

        return ResponseEntity.ok(otherService.addOthers(otherRequest));

    }

    @GetMapping("/others/{otherId}")
    public ResponseEntity<OtherResponse> getOtherById(@PathVariable Long otherId) {

        return ResponseEntity.ok(otherService.getOtherById(otherId));

    }

    @PutMapping("/others/{otherId}")
    public ResponseEntity<OtherResponse> updateOtherById(
            @PathVariable Long otherId,
            @RequestBody @Valid OtherRequest otherRequest
    ) {

        return ResponseEntity.ok(otherService.updateOtherById(otherId, otherRequest));

    }

    @DeleteMapping("/others/{otherId}")
    public ResponseEntity<String> deleteOtherById (@PathVariable Long otherId) {

        return ResponseEntity.ok(otherService.deleteOtherById(otherId));

    }

}
