package com.harshil_infotech.hire_genie.exception.handler;

import com.harshil_infotech.hire_genie.dto.error.response.ErrorResponse;
import com.harshil_infotech.hire_genie.exception.InvalidDateRangeException;
import com.harshil_infotech.hire_genie.exception.EndDateRequiredException;
import com.harshil_infotech.hire_genie.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EndDateRequiredException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleProjectEndDateRequiredException (
            EndDateRequiredException e,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleInvalidProjectDateRangeException (
            InvalidDateRangeException e,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), request.getRequestURI());
    }

    // Fallback for any unhandled exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(
            Exception e,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException (
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
    }

}
