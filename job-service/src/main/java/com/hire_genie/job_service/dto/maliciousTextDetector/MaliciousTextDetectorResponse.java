package com.hire_genie.job_service.dto.maliciousTextDetector;

import com.hire_genie.job_service.enums.Category;
import com.hire_genie.job_service.enums.Verdict;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MaliciousTextDetectorResponse(

        @NotNull
        Verdict verdict,

        @NotNull
        Category category,

        @NotNull
        String reason

) {
}
