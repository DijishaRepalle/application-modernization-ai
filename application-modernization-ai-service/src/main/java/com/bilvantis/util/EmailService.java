package com.bilvantis.util;

import com.bilvantis.model.UserInformationDTO;

public interface EmailService {
    void sendMailOtpGeneration(EmailDetails emailDetails, UserInformationDTO worker);

}
