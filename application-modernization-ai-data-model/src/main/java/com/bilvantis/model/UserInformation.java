package com.bilvantis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation extends BaseDTO {
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