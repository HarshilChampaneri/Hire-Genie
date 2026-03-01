package com.harshil_infotech.hire_genie.controller;

import com.harshil_infotech.hire_genie.dto.other.request.OtherRequest;
import com.harshil_infotech.hire_genie.dto.other.response.OtherResponse;
import com.harshil_infotech.hire_genie.service.OtherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
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
