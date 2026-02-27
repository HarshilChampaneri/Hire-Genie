package com.harshil_infotech.hire_genie.dto.experience.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.YearMonth;
import java.util.List;

@Builder
public record ExperienceRequest(

        @NotBlank
        @Length(
                min = 1,
                max = 150,
                message = "Name of the company must be between 1 to 150 characters."
        )
        String companyName,

        @NotBlank
        @Length(
                min = 1,
                max = 150,
                message = "Position must be between 1 to 150 characters."
        )
        String position,

        @NotNull(message = "Experience Start Date can't be null.")
        YearMonth startDate,

        Boolean isWorkingInCompany,

        YearMonth endDate,

        @NotEmpty(message = "Experience Description can't be empty.")
        List<String> description
) {
}
