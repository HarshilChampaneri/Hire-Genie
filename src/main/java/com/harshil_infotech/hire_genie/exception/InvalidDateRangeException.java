package com.harshil_infotech.hire_genie.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String resourceName) {
        super(resourceName + " end date must be after the " + resourceName + " start date.");
    }
}
