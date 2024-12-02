package com.bilvantis.repository;

public interface CustomUserInformationRepository {
    Integer saveOtpAndOtpGenerationTime(String phoneNumber, String otp, Long otpGenerationTime);

    String findRoleByUserId(String userId);
}

