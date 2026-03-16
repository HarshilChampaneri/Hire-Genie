package com.hire_genie.resume_builder.service;

import java.util.List;
import java.util.Set;

public interface SecurityService {
    String getCurrentLoggedInUser();

    Set<String> getAuthorities();
}
