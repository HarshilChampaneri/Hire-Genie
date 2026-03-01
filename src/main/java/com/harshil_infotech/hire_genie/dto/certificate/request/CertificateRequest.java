package com.harshil_infotech.hire_genie.dto.certificate.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CertificateRequest(

        @NotBlank
        @Length(min = 2, max = 150, message = "Title of the certificate must be between 2 to 150 characters.")
        String certificateTitle,

        @NotBlank(message = "certificate url can't be blank")
        String certificateUrl

) {
}
