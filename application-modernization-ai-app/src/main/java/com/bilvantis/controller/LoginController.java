package com.bilvantis.controller;

import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserResponseDTO;
import com.bilvantis.service.LoginService;
import com.bilvantis.util.AppModernizationAPIConstants;
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
    private final LoginService<UserInformation> loginService;

    @Autowired
    public LoginController(LoginService<UserInformation> loginService) {
        this.loginService = loginService;
    }

    /**
     * Sends a one-time password (OTP) to the provided phone number for authentication purposes.
     *
     * This endpoint triggers the generation and sending of a one-time password (OTP) to the
     * specified phone number. The OTP is typically used for verifying the identity of a worker
     * or user during login or a specific authentication process. The response contains a success
     * message if the OTP is successfully sent.
     *
     * @param phoneNumber The phone number to which the one-time password will be sent.
     *                    This should be a valid, non-null phone number.
     * @return ResponseEntity containing a {@link UserResponseDTO} with the result of the operation,
     *         including a success message and HTTP status 200 (OK).
     * @throws IllegalArgumentException If the phone number is null or invalid.
     */
    @PostMapping("/one-time-password")
    public ResponseEntity<UserResponseDTO> sendUserOneTimePassword(@NotNull @RequestParam String phoneNumber) {
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(loginService.sendOneTimePasswordMail(phoneNumber), null, null, null, null, AppModernizationAPIConstants.SUCCESS), HttpStatus.OK);

    }

    /**
     * Verifies the worker's login details by validating the provided phone number and one-time password (OTP).
     *
     * This endpoint handles the authentication of a worker by checking the provided phone number and OTP.
     * If the details are valid, the worker's information is retrieved and included in the response.
     * Additionally, the response includes HTTP headers containing relevant authentication tokens or data.
     *
     * @param phoneNumber The phone number of the worker attempting to log in. This should be a non-blank value.
     * @param otp The one-time password (OTP) entered by the worker. This should be a non-blank value.
     * @return ResponseEntity containing a {@link UserResponseDTO} with the worker's information,
     *         authentication status, and a set of HTTP headers. The HTTP status is 200 (OK) if authentication is successful.
     * @throws IllegalArgumentException If either the phone number or OTP is blank or invalid.
     */
    @GetMapping("/auth")
    public ResponseEntity<UserResponseDTO> verifyUserDetails(@NotBlank @RequestParam String phoneNumber, @NotBlank @RequestParam String otp) {
        UserInformation userInformation = loginService.verifyUserLogin(phoneNumber, otp);
        HttpHeaders headers = loginService.setHeader((userInformation.getId()));
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(userInformation, null, null, null, null, AppModernizationAPIConstants.SUCCESS), headers, HttpStatus.OK);
    }

}