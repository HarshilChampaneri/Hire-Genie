package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.other.request.OtherRequest;
import com.hire_genie.resume_builder.dto.other.response.OtherResponse;
import com.hire_genie.resume_builder.exception.ResourceNotFoundException;
import com.hire_genie.resume_builder.mapper.OtherMapper;
import com.hire_genie.resume_builder.model.Other;
import com.hire_genie.resume_builder.repository.OtherRepository;
import com.hire_genie.resume_builder.service.OtherService;
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
