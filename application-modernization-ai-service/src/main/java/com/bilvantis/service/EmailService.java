package com.bilvantis.service;

import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.util.EmailDetails;

public interface EmailService {
    void sendMailOtpGeneration(EmailDetails emailDetails, UserInformationDTO worker);

    void sendNotificationEmail(String processName, String templatePath, String subject);

    void sendStartNotificationEmail(String processName);

    void sendCodeRevampStartNotificationEmail(String processName);
}
