package com.harshil_infotech.hire_genie.service;

import com.harshil_infotech.hire_genie.dto.other.request.OtherRequest;
import com.harshil_infotech.hire_genie.dto.other.response.OtherResponse;

public interface OtherService {
    OtherResponse addOthers(OtherRequest otherRequest);

    OtherResponse getOtherById(Long otherId);

    OtherResponse updateOtherById(Long otherId, OtherRequest otherRequest);

    String deleteOtherById(Long otherId);
}
