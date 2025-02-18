package com.bilvantis.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@ComponentScan
@PropertySource("classpath:appmodernization.properties")
public class AppModernizationProperties {

    @Value("${login.otp-format-percentage}")
    private String otpFormatPercentage;

    @Value("${login.otp-format-numberofdigits-bound}")
    private String otpNoOfDigitsBound;


    @Value("${email.subject-otp-generation}")
    private String subjectForOtpGeneration;

    @Value("${login.otp-sent-mail}")
    private String usersOtpSentViaMail;

    @Value("${exception.error.message}")
    private String exceptionErrorMessage;

    @Value("${login.otp.expired}")
    private String loginOtpExpired;

    @Value("${login.phone-number-otp-mismatch}")
    private String usersPhoneNumberOrOTPMismatch;

    @Value("${users.not-available}")
    private String usersNotAvailable;

    @Value("${spring.mail.username}")
    private String senderMailId;

    @Value("${distribution-list}")
    private String distributionList;

}
