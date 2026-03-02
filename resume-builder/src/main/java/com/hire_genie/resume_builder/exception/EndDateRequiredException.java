package com.hire_genie.resume_builder.exception;

public class EndDateRequiredException extends RuntimeException {
    public EndDateRequiredException(String resourceName) {
        super(resourceName + " end date is required when the " + resourceName + " is not in progress.");
    }
}
