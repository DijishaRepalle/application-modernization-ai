package com.bilvantis.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserInformationDTO extends BaseDTO {
    @Id
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String otp;
    private Long otpGenerationTime;
    private String role;
}