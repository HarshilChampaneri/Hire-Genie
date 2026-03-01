package com.harshil_infotech.hire_genie.mapper;

import com.harshil_infotech.hire_genie.dto.other.request.OtherRequest;
import com.harshil_infotech.hire_genie.dto.other.response.OtherResponse;
import com.harshil_infotech.hire_genie.model.Other;
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
