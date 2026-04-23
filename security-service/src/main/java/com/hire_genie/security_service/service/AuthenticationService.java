package com.hire_genie.security_service.service;

import com.hire_genie.security_service.dto.*;
import com.hire_genie.security_service.entity.User;
import com.hire_genie.security_service.enums.Role;
import com.hire_genie.security_service.exception.UnauthorizedAdminException;
import com.hire_genie.security_service.exception.UserAlreadyExistsException;
import com.hire_genie.security_service.jwt.JwtService;
import com.hire_genie.security_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;
    private final OtpService otpService;
    private final EmailService emailService;

    @Value("${app.security.admin-secret}")
    private String internalAdminSecret;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("An account with this email already exists.");
        }

        if (request.roles().contains(Role.ROLE_ADMIN)) {
            if (request.adminSecret() == null || !request.adminSecret().equals(internalAdminSecret)) {
                throw new UnauthorizedAdminException("Unauthorized: Invalid Admin Secret Provided.");
            }
        }

        var user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(request.roles())
                .isActive(true)
                .build();

        user = userRepository.save(user);

        var userDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public MessageResponse authenticate(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String otp = otpService.generateAndStoreOtp(request.email());
        emailService.sendOtpEmail(request.email(), otp);

        return MessageResponse.builder()
                .message("OTP sent to your registered email address. Please verify to complete login.")
                .build();
    }

    public AuthResponse verifyOtp(OtpVerificationRequest request) {

        if (!otpService.verifyOtp(request.email(), request.otp())) {
            throw new IllegalArgumentException("Invalid or expired OTP. Please try again.");
        }

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with email: " + request.email() + " is not found."
                ));

        var userDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void logout(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing of malformed Authorization header.");
        }

        String token = authHeader.substring(7);
        Long expiryMillis = jwtService.extractExpiration(token).getTime();
        tokenBlacklistService.blacklist(token, expiryMillis);

    }
}
