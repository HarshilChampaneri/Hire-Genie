package com.hire_genie.resume_builder.dto.education.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.YearMonth;

@Builder
public record EducationRequest(

        @NotBlank
        @Length(
                min = 2,
                max = 150,
                message = "Title of the education must be between 2 to 150 characters."
        )
        String educationTitle,

        @NotBlank(message = "location can't be blank.")
        String location,

        @NotBlank(message = "field of study can't be blank.")
        String fieldOfStudy,

        @NotNull(message = "Education start date can't be null.")
        YearMonth startDate,
        Boolean isEducationInProgress,

        @NotNull(message = "Education end date can't be null.")
        YearMonth endDate,

        String gradeTitle,

        Double grades
) {
}
