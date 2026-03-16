package com.hire_genie.resume_builder.security.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoggedInUser {

    public String getCurrentLoggedInUser() {

        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

    }

    public Set<String> getAuthorities() {

        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

    }

}
