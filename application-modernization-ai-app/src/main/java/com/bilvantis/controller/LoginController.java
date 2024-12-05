package com.bilvantis.controller;

import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserResponseDTO;
import com.bilvantis.service.LoginService;
import com.bilvantis.util.ModernizationAppConstants;
import com.bilvantis.util.UserRequestResponseBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Sends a one-time password (OTP) to the user's phone number.
     *
     * @param phoneNumber
     * @return user details
     */
    @PostMapping("/one-time-password")
    public ResponseEntity<UserResponseDTO> sendWorkerOneTimePassword(@NotNull @RequestParam String phoneNumber) {
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(loginService.sendOneTimePasswordMail(phoneNumber), null, null, null, null, ModernizationAppConstants.SUCCESS), HttpStatus.OK);
    }

    /**
     * verifies user login details
     *
     * @param phoneNumber
     * @param otp
     * @return user details
     */
    @GetMapping("/auth")
    public ResponseEntity<UserResponseDTO> authLoginDetails(@NotBlank @RequestParam String phoneNumber, @NotBlank @RequestParam String otp) {
        UserInformation userInformation = loginService.verifyWorkerLogin(phoneNumber, otp);
        HttpHeaders headers = loginService.setHeader(userInformation.getId());
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(userInformation, null, null, null, null, ModernizationAppConstants.SUCCESS), headers, HttpStatus.OK);
    }

}
