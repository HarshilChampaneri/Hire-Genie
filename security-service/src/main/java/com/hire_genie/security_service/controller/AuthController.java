package com.hire_genie.security_service.controller;

import com.hire_genie.security_service.dto.*;
import com.hire_genie.security_service.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/security/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody OtpVerificationRequest request) {
        return ResponseEntity.ok(service.verifyOtp(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        service.logout(authHeader);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-registration-otp")
    public ResponseEntity<MessageResponse> sendRegistrationOtp(
            @Valid @RequestBody RegistrationOtpRequest request) {
        return ResponseEntity.ok(service.sendRegistrationOtp(request));
    }

}
