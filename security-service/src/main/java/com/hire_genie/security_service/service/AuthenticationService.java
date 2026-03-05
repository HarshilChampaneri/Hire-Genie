package com.hire_genie.security_service.service;

import com.hire_genie.security_service.dto.AuthRequest;
import com.hire_genie.security_service.dto.AuthResponse;
import com.hire_genie.security_service.dto.RegisterRequest;
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

    public AuthResponse authenticate(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + request.email() + " is not found."));

        var userDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
