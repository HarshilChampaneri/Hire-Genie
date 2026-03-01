package com.harshil_infotech.hire_genie.service.impl;

import com.harshil_infotech.hire_genie.dto.other.request.OtherRequest;
import com.harshil_infotech.hire_genie.dto.other.response.OtherResponse;
import com.harshil_infotech.hire_genie.exception.ResourceNotFoundException;
import com.harshil_infotech.hire_genie.mapper.OtherMapper;
import com.harshil_infotech.hire_genie.model.Other;
import com.harshil_infotech.hire_genie.repository.OtherRepository;
import com.harshil_infotech.hire_genie.service.OtherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtherServiceImpl implements OtherService {

    private final OtherRepository otherRepository;
    private final OtherMapper otherMapper;

    @Override
    public OtherResponse addOthers(OtherRequest otherRequest) {

        Other other = otherMapper.toOtherFromOtherRequest(otherRequest);
        other.setIsDeleted(false);

        return otherMapper.toOtherResponseFromOther(otherRepository.save(other));
    }

    @Override
    public OtherResponse getOtherById(Long otherId) {

        Other other = otherRepository.findById(otherId).orElseThrow(() ->
                new ResourceNotFoundException("other", otherId));

        if (other.getIsDeleted()) {
            throw new ResourceNotFoundException("other", otherId);
        }

        return otherMapper.toOtherResponseFromOther(otherRepository.save(other));

    }

    @Override
    public OtherResponse updateOtherById(Long otherId, OtherRequest otherRequest) {

        Other other = otherRepository.findById(otherId).orElseThrow(() ->
                new ResourceNotFoundException("other", otherId));

        if (other.getIsDeleted()) {
            throw new ResourceNotFoundException("other", otherId);
        }

        if (!otherRequest.description().isEmpty() && !otherRequest.description().equals(other.getDescription())) {
            other.setDescription(otherRequest.description());
        }

        return otherMapper.toOtherResponseFromOther(otherRepository.save(other));

    }

    @Override
    public String deleteOtherById(Long otherId) {

        Other other = otherRepository.findById(otherId).orElseThrow(() ->
                new ResourceNotFoundException("other", otherId));

        if (other.getIsDeleted()) {
            return "Other with otherId: " + otherId + " is already deleted Before.";
        }

        other.setIsDeleted(true);
        otherRepository.save(other);

        return "Other with otherId : " + otherId + " deleted successfully.";
    }

}
