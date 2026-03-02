package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.other.request.OtherRequest;
import com.hire_genie.resume_builder.dto.other.response.OtherResponse;

public interface OtherService {
    OtherResponse addOthers(OtherRequest otherRequest);

    OtherResponse getOtherById(Long otherId);

    OtherResponse updateOtherById(Long otherId, OtherRequest otherRequest);

    String deleteOtherById(Long otherId);
}
