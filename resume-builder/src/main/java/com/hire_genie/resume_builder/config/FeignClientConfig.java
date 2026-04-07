package com.hire_genie.resume_builder.config;

import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                String internalSecret = request.getHeader("X-Internal-Secret");
                String userEmail = request.getHeader("X-User-Email");
                String userRoles = request.getHeader("X-User-Roles");

                if (internalSecret != null) {
                    requestTemplate.header("X-Internal-Secret", internalSecret);
                }
                if (userEmail != null) {
                    requestTemplate.header("X-User-Email", userEmail);
                }
                if (userRoles != null) {
                    requestTemplate.header("X-User-Roles", userRoles);
                }
            }
        };
    }
}
