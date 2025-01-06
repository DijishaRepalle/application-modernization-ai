package com.bilvantis.repository;

import com.bilvantis.model.UserInformation;

import java.util.Optional;

public interface CustomUserInformationRepository {
    Integer saveOtpAndOtpGenerationTime(String phoneNumber, String otp, Long otpGenerationTime);

    String findRoleByUserId(String userId);

    Optional<UserInformation> findByEmail(String email);

    UserInformation editUser(String email,UserInformation updatedUserDetails);

    void delete(String email);
}

