package com.hire_genie.resume_builder.service;

import com.hire_genie.resume_builder.dto.other.request.OtherRequest;
import com.hire_genie.resume_builder.dto.other.response.OtherResponse;

public interface OtherService {
    OtherResponse addOthers(OtherRequest otherRequest);

    OtherResponse getOther() throws Exception;

    OtherResponse updateOther(OtherRequest otherRequest) throws Exception;

    String deleteOther();
}
