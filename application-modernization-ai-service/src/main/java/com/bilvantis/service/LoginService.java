package com.bilvantis.service;

import com.bilvantis.model.UserInformation;
import org.springframework.http.HttpHeaders;

public interface LoginService<I extends UserInformation> {
    String sendOneTimePasswordMail(String phoneNumber);

    I verifyWorkerLogin(String phoneNumber, String otp);

    HttpHeaders setHeader(String userId);
}
