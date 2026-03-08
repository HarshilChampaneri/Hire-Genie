package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final LoggedInUser loggedInUser;

    @Override
    public String getCurrentLoggedInUser() {

        return loggedInUser.getCurrentLoggedInUser();

    }

}
