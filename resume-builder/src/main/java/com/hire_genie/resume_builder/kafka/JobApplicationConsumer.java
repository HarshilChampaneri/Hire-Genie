package com.hire_genie.resume_builder.kafka;

import com.hire_genie.resume_builder.dto.profile.response.ProfileResponse;
import com.hire_genie.resume_builder.kafkaEvent.CandidateProfileEvent;
import com.hire_genie.resume_builder.kafkaEvent.JobApplicationEvent;
import com.hire_genie.resume_builder.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.hire_genie.resume_builder.util.StaticConstants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobApplicationConsumer {

    private final ProfileService profileService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = JOB_APPLICATION_REQUESTS, groupId = CANDIDATE_SERVICE_GROUP)
    public void consumeApplicationRequest(JobApplicationEvent jobApplicationEvent) throws Exception {
        log.info("Received application request for: {}", jobApplicationEvent.candidateEmail());

        ProfileResponse profile = profileService.getProfileByEmail(jobApplicationEvent.candidateEmail());

        CandidateProfileEvent responseEvent = new CandidateProfileEvent(jobApplicationEvent.jobId(), profile);
        kafkaTemplate.send(CANDIDATE_PROFILE_RESPONSES, profile.email(), responseEvent);

        log.info("Application request sent for: {}", jobApplicationEvent.candidateEmail());
    }

}
