package com.hire_genie.api_gateway.security;

import com.hire_genie.api_gateway.util.MutableHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Value("${app.security.internal-secret}")
    private String headerSecret;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/security/auth/login",
            "/api/security/auth/register",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean isPublicPath = PUBLIC_PATHS.stream().anyMatch(path::contains);

        // Check for public paths
        if (isPublicPath) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response, "Missing Token");
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.isTokenValid(token)) {
                sendUnauthorized(response, "Token Expired");
                return;
            }

            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
            // Propagate identity to downstream services
            mutableRequest.addHeader("X-User-Email", jwtService.extractUsername(token));
            mutableRequest.addHeader("X-User-Roles", jwtService.extractRoles(token));
            mutableRequest.addHeader("X-Internal-Secret", headerSecret);

            filterChain.doFilter(mutableRequest, response);
        } catch (Exception e) {
            sendUnauthorized(response, "Invalid Token: " + e.getMessage());
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"error\": \"Unauthorized\", \"message\": \"%s\"}".formatted(message)
        );
    }

}
