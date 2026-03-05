package com.hire_genie.security_service.exception;

public class InvalidTokenException extends SecurityServiceException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
