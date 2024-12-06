package com.bilvantis.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public class UserInformationDTO extends BaseDTO {
    @Id
    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String otp;
    private Long otpGenerationTime;
    private String role;
}