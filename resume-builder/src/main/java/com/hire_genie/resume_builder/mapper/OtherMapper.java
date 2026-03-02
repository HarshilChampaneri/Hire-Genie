package com.hire_genie.resume_builder.mapper;

import com.hire_genie.resume_builder.dto.other.request.OtherRequest;
import com.hire_genie.resume_builder.dto.other.response.OtherResponse;
import com.hire_genie.resume_builder.model.Other;
import org.springframework.stereotype.Component;

@Component
public class OtherMapper {

    public Other toOtherFromOtherRequest(OtherRequest otherRequest) {
        return Other.builder()
                .description(!otherRequest.description().isEmpty() ? otherRequest.description() : null)
                .build();
    }

    public OtherResponse toOtherResponseFromOther(Other other) {
        return OtherResponse.builder()
                .otherId(other.getOtherId())
                .description(other.getDescription())
                .build();
    }

}
