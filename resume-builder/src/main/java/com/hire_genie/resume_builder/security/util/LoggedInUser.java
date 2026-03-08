package com.hire_genie.resume_builder.security.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoggedInUser {

    public String getCurrentLoggedInUser() {

        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

    }

}
