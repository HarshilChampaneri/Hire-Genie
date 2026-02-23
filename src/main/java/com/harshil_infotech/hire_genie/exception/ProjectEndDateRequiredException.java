package com.harshil_infotech.hire_genie.exception;

public class ProjectEndDateRequiredException extends RuntimeException {
    public ProjectEndDateRequiredException() {
        super("Project end date is required when the project is not in progress.");
    }
}
