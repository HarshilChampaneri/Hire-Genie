package com.hire_genie.job_service.exception.handler;

import com.hire_genie.job_service.dto.error.response.ErrorResponse;
//import com.hire_genie.job_service.exception.EndDateRequiredException;
//import com.hire_genie.job_service.exception.InvalidDateRangeException;
import com.hire_genie.job_service.exception.InvalidAccessException;
import com.hire_genie.job_service.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(EndDateRequiredException.class)
//    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
//    public ErrorResponse handleProjectEndDateRequiredException (
//            EndDateRequiredException e,
//            HttpServletRequest request
//    ) {
//        return ErrorResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), request.getRequestURI());
//    }

//    @ExceptionHandler(InvalidDateRangeException.class)
//    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
//    public ErrorResponse handleInvalidProjectDateRangeException (
//            InvalidDateRangeException e,
//            HttpServletRequest request
//    ) {
//        return ErrorResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), request.getRequestURI());
//    }

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

    @ExceptionHandler(InvalidAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidAccessException (
            InvalidAccessException e,
            HttpServletRequest request
    ) {
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage(), request.getRequestURI());
    }

}
