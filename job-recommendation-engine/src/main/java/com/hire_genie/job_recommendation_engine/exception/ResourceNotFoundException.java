package com.hire_genie.job_recommendation_engine.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(resourceName + " not found with id: " + resourceId);
    }

    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " not found");
    }
}
