package com.hire_genie.security_service.exception;

public class UserAlreadyExistsException extends SecurityServiceException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
