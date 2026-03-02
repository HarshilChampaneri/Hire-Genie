package com.hire_genie.resume_builder.annotation;

import com.hire_genie.resume_builder.dto.project.request.ProjectRequest;
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
