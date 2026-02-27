package com.harshil_infotech.hire_genie.exception;

public class EndDateRequiredException extends RuntimeException {
    public EndDateRequiredException(String resourceName) {
        super(resourceName + " end date is required when the " + resourceName + " is not in progress.");
    }
}
