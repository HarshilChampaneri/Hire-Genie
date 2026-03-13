package com.hire_genie.resume_builder.service.impl;

import com.hire_genie.resume_builder.dto.other.request.OtherRequest;
import com.hire_genie.resume_builder.dto.other.response.OtherResponse;
import com.hire_genie.resume_builder.mapper.OtherMapper;
import com.hire_genie.resume_builder.model.Other;
import com.hire_genie.resume_builder.repository.OtherRepository;
import com.hire_genie.resume_builder.security.util.LoggedInUser;
import com.hire_genie.resume_builder.service.OtherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtherServiceImpl implements OtherService {

    private final OtherRepository otherRepository;
    private final OtherMapper otherMapper;
    private final LoggedInUser loggedInUser;

    @Override
    public OtherResponse addOthers(OtherRequest otherRequest) {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Other existingOther = otherRepository.findActiveOther(userEmail);

        if (existingOther != null) {

            List<String> description = otherRequest.description();
            for (String otherDescription : description) {
                existingOther.getDescription().add(otherDescription);
            }
            existingOther.setIsDeleted(false);
            existingOther.setUserEmail(loggedInUser.getCurrentLoggedInUser());

            return otherMapper.toOtherResponseFromOther(otherRepository.save(existingOther));
        }

        Other newOther = otherMapper.toOtherFromOtherRequest(otherRequest);
        newOther.setIsDeleted(false);
        newOther.setUserEmail(userEmail);

        return otherMapper.toOtherResponseFromOther(otherRepository.save(newOther));
    }

    @Override
    public OtherResponse getOther() throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Other other = otherRepository.findActiveOther(userEmail);
        if (other == null) {
            throw new Exception("No Other found.");
        }

        return otherMapper.toOtherResponseFromOther(otherRepository.save(other));

    }

    @Override
    public OtherResponse updateOther(OtherRequest otherRequest) throws Exception {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Other other = otherRepository.findActiveOther(userEmail);
        if (other == null) {
            throw new Exception("No Other found.");
        }

        if (!otherRequest.description().isEmpty() && !otherRequest.description().equals(other.getDescription())) {
            other.setDescription(otherRequest.description());
        }
        other.setIsDeleted(false);
        other.setUserEmail(loggedInUser.getCurrentLoggedInUser());

        return otherMapper.toOtherResponseFromOther(otherRepository.save(other));
    }

    @Override
    public String deleteOther() {

        String userEmail = loggedInUser.getCurrentLoggedInUser();

        Other other = otherRepository.findActiveOther(userEmail);
        if (other == null) {
            return "There is no Other associated with your account. Please create one before deleting.";
        }

        other.setIsDeleted(true);
        otherRepository.save(other);

        return "Other with otherId : " + other.getOtherId() + " deleted successfully.";
    }

}
