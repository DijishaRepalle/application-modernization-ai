package com.bilvantis.serviceImpl;

import com.bilvantis.exception.ApplicationException;
import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.repository.UserInformationRepository;
import com.bilvantis.service.LoginService;
import com.bilvantis.util.AppModernizationEngineSupport;
import com.bilvantis.util.AppModernizationProperties;
import com.bilvantis.util.EmailService;
import com.bilvantis.util.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bilvantis.util.AppModernizationAPIConstants.*;
import static com.bilvantis.util.UserInformationSupport.convertUsersDTOTOUsersEntity;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;

    private final UserInformationRepository userInformationRepository;

    private final AppModernizationEngineSupport appModernizationEngineSupport;
    private final AppModernizationProperties appModernizationProperties;

    @Autowired
    public LoginServiceImpl(JwtTokenService jwtTokenService, EmailService emailService, UserInformationRepository userInformationRepository, AppModernizationEngineSupport appModernizationEngineSupport, AppModernizationProperties appModernizationProperties) {
        this.jwtTokenService = jwtTokenService;
        this.emailService = emailService;
        this.userInformationRepository = userInformationRepository;
        this.appModernizationEngineSupport = appModernizationEngineSupport;
        this.appModernizationProperties = appModernizationProperties;
    }


    @Override
    public String sendOneTimePasswordMail(String phoneNumber) {
        try {
            if (!Predicates.isValidPhoneNumber.test(phoneNumber)) {
                throw new ApplicationException(INVALID_PHONE_NUMBER);
            }
            UserInformationDTO dto = userInformationRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> {
                log.error(PHONE_NUMBER_NOT_EXIST);
                return new ApplicationException(PHONE_NUMBER_NOT_EXIST);
            });
            String randomSixDigits = appModernizationEngineSupport.generateRandomSixDigits();
            dto.setOtp(randomSixDigits);
            dto.setOtpGenerationTime(System.currentTimeMillis());
            // Sending OTP email asynchronously
            appModernizationEngineSupport.sendOtpEmailAsync(dto);
            // Saving OTP and its generation time
            userInformationRepository.saveOtpAndOtpGenerationTime(phoneNumber, dto.getOtp(), dto.getOtpGenerationTime());
            return appModernizationProperties.getUsersOtpSentViaMail();

        } catch (DataAccessException e) {
            log.error(String.format(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName()), e);
            throw new ApplicationException(e.getMessage());
        }
    }


    @Override
    public UserInformation verifyWorkerLogin(String phoneNumber, String otp) {
        try {
            if (ObjectUtils.isEmpty(phoneNumber)) {
                throw new ApplicationException(PHONE_NUMBER_NOT_FOUND);
            }

            if (ObjectUtils.isEmpty(otp)) {
                throw new ApplicationException(OTP_NOT_FOUND);
            }
            Optional<UserInformationDTO> number = userInformationRepository.findByPhoneNumber(phoneNumber);
            if (number.isPresent()) {
                UserInformationDTO dto = number.get();
                // Check for OTP expiration
                long otpExpiryTime = dto.getOtpGenerationTime() + ONE_MINUTE_IN_MILLI_SECONDS;
                if (System.currentTimeMillis() > otpExpiryTime) {
                    throw new ApplicationException(appModernizationProperties.getLoginOtpExpired());
                }
                if (StringUtils.equals(dto.getOtp(), otp)) {
                    return convertUsersDTOTOUsersEntity(dto);
                }
                throw new ApplicationException(appModernizationProperties.getWorkerUserPhoneNumberOrOTPMismatch());
            }
            throw new ApplicationException(appModernizationProperties.getWorkersNotAvailable());

        } catch (DataAccessException e) {
            log.error(String.format(appModernizationProperties.getExceptionErrorMessage(), this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName()), e);
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public HttpHeaders setHeader(String userId) {
        HttpHeaders headers = new HttpHeaders();
        String token = jwtTokenService.generateToken(userId);
        headers.add(TOKEN, token);
        return headers;
    }
}
