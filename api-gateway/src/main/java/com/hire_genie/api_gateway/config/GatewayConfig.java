package com.hire_genie.api_gateway.config;

import com.hire_genie.api_gateway.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtService jwtService;

    @Value("${app.security.internal-secret}")
    private String internalSecret;

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route("security-service")
                .route(path("/api/security/**"), http())
                        .filter(lb("SECURITY-SERVICE"))
                        .before(this::addSecurityHeaders)
                        .build()
                .and(route("resume-builder")
                        .route(path("/api/resumes/**"), http())
                        .filter(lb("RESUME-BUILDER"))
                        .before(this::addSecurityHeaders)
                        .build())
                .and(route("job-service")
                        .route(path("/api/jobs/**"), http())
                        .filter(lb("JOB-SERVICE"))
                        .before(this::addSecurityHeaders)
                        .build());
    }

    private ServerRequest addSecurityHeaders(ServerRequest request) {
        String path = request.uri().getPath();

        if (path.contains("/auth/login") ||
            path.contains("/auth/register") ||
            path.contains("/auth/verify-otp") ||
            path.contains("/v3/api-docs") ||
            path.contains("/swagger-ui")
        ) {

            return ServerRequest.from(request)
                    .header("X-Internal-Secret", internalSecret)
                    .build();

        }

        String authHeader = request.headers().firstHeader("Authorization");
        var requestBuilder = ServerRequest.from(request)
                .header("X-Internal-Secret", internalSecret);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtService.isTokenValid(token)) {
                    requestBuilder.header("X-User-Email", jwtService.extractUsername(token));
                    requestBuilder.header("X-User-Roles", jwtService.extractRoles(token));
                    log.info("Identity headers prepared for path: {}", path);
                }
            } catch (Exception e) {
                log.error("Token validation error: {}", e.getMessage());
            }
        }

        return requestBuilder.build();
    }
}
