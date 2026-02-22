package com.harshil_infotech.hire_genie.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProjectEndDateValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProjectEndDate {
    String message() default "Project end date is required when project is not in progress.";
    Class <?>[] groups() default {};
    Class <? extends Payload>[] payload() default {};
}
