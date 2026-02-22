package com.harshil_infotech.hire_genie.annotation;

import com.harshil_infotech.hire_genie.dto.request.ProjectRequest;
import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidatorContext;

public class ProjectEndDateValidator implements ConstraintValidator<ValidProjectEndDate, ProjectRequest> {

    @Override
    public boolean isValid(ProjectRequest request, ConstraintValidatorContext context) {
        boolean isInProgress = request.isProjectInProgress() != null && request.isProjectInProgress();

        // Is project is NOT in progress, endDate is required
        if (!isInProgress && request.projectEndDate() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Project end date is required when project is not in progress.")
                    .addPropertyNode("projectEndDate")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
