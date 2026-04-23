package com.hire_genie.security_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpService {

    private static final String OTP_PREFIX = "otp:";
    private static final int OTP_TTL_MINUTES = 5;
    private static final int OTP_LENGTH = 6;

    private final StringRedisTemplate redisTemplate;
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateAndStoreOtp(String email) {

        String otp = String.format("%0" + OTP_LENGTH + "d",
                secureRandom.nextInt((int) Math.pow(10, OTP_LENGTH)));

        String key = OTP_PREFIX + email;
        redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(OTP_TTL_MINUTES));
        log.info("OTP generated and stored in Redis for email: {}", email);

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String key = OTP_PREFIX + email;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp == null) {
            log.warn("OTP not found or expired for email: {}", email);
            return false;
        }

        if (!storedOtp.equals(otp)) {
            log.warn("Invalid OTP attempt for email: {}", email);
            return false;
        }

        redisTemplate.delete(key);
        log.info("OTP verified and deleted for email: {}", email);
        return true;
    }

}
