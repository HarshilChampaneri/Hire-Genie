package com.hire_genie.security_service.service;

import com.hire_genie.security_service.dto.AuthRequest;
import com.hire_genie.security_service.dto.AuthResponse;
import com.hire_genie.security_service.dto.RegisterRequest;
import com.hire_genie.security_service.entity.User;
import com.hire_genie.security_service.jwt.JwtService;
import com.hire_genie.security_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

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
                .orElseThrow();

        var userDetails = new CustomUserDetails(user);
        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
