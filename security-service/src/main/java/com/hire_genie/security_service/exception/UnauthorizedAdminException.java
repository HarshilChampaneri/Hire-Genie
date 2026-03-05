package com.hire_genie.security_service.exception;

public class UnauthorizedAdminException extends SecurityServiceException{

    public UnauthorizedAdminException(String message) {
        super(message);
    }
}
