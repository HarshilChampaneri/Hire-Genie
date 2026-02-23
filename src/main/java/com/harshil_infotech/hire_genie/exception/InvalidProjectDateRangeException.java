package com.harshil_infotech.hire_genie.exception;

public class InvalidProjectDateRangeException extends RuntimeException {
    public InvalidProjectDateRangeException() {
        super("Project end date must be after the project start date.");
    }
}
