package com.harshil_infotech.hire_genie.dto.project.request;

import com.harshil_infotech.hire_genie.annotation.ValidProjectEndDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.YearMonth;
import java.util.List;

@Builder
@ValidProjectEndDate
public record ProjectRequest(

        @NotBlank
        @Length(
                min = 2,
                max = 150,
                message = "Name of the project must be between 2 to 150 characters"
        )
        String projectName,

        @NotBlank(message = "projectUrl can't be blank.")
        @Pattern(
                regexp = "^(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)$",
                message = "Invalid URL format"
        )
        String projectUrl,

        @NotEmpty(message = "Project Technology Stack can't be empty.")
        List<String> projectTechStacks,

        @NotNull(message = "Project Start Date can't be null.")
        YearMonth projectStartDate,

        Boolean isProjectInProgress,

        YearMonth projectEndDate,

        @NotEmpty(message = "Project Description can't be empty.")
        List<String> projectDescription
) {
}
