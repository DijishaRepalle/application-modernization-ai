package com.bilvantis.util;

import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.bilvantis.util.EmailSupport.settingEmailDetails;

@Component
@Slf4j
public class AppModernizationEngineSupport {
    private final AppModernizationProperties appModernizationProperties;

    private final EmailService emailService;
    private final EmailDetails emailDetails;

    public AppModernizationEngineSupport(AppModernizationProperties appModernizationProperties, EmailService emailService, EmailDetails emailDetails) {
        this.appModernizationProperties = appModernizationProperties;
        this.emailService = emailService;
        this.emailDetails = emailDetails;
    }

    /**
     * Generates a random six-digit OTP (One-Time Password).
     *
     * @return A randomly generated six-digit OTP.
     */
    public String generateRandomSixDigits() {
        Random random = new Random();
        return String.format(appModernizationProperties.getOtpFormatPercentage(), random.nextInt(Integer.parseInt(appModernizationProperties.getOtpNoOfDigitsBound())));
    }

    /**
     * Asynchronously sends an OTP (One-Time Password) email.
     *
     * @param dto UserInformationDTO
     */
    @Async
    public void sendOtpEmailAsync(UserInformationDTO dto) {
        EmailDetails emailDetails = settingEmailDetails(dto.getEmail(), appModernizationProperties.getSubjectForOtpGeneration());
        emailService.sendMailOtpGeneration(emailDetails, dto);
    }

}
