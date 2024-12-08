package com.bilvantis.util;

import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;

public class UserInformationSupport {
    public static UserInformationDTO convertUserEntityTOUserDTO(UserInformation userInformation) {
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setId(userInformation.getId());
        userInformationDTO.setEmail(userInformation.getEmail());
        userInformationDTO.setPassword(userInformation.getPassword());
        userInformationDTO.setFirstName(userInformation.getFirstName());
        userInformationDTO.setLastName(userInformation.getLastName());
        userInformationDTO.setPhoneNumber(userInformation.getPhoneNumber());
        userInformationDTO.setOtp(userInformation.getOtp());
        userInformationDTO.setOtpGenerationTime(userInformation.getOtpGenerationTime());
        userInformationDTO.setRole(userInformation.getRole());
        return userInformationDTO;
    }

    public static UserInformation convertUsersDTOTOUsersEntity(UserInformationDTO userInformationDTO) {
        UserInformation userInformation = new UserInformation();
        userInformation.setId(userInformationDTO.getId());
        userInformation.setEmail(userInformationDTO.getEmail());
        userInformation.setPassword(userInformationDTO.getPassword());
        userInformation.setFirstName(userInformationDTO.getFirstName());
        userInformation.setLastName(userInformationDTO.getLastName());
        userInformation.setPhoneNumber(userInformationDTO.getPhoneNumber());
        userInformation.setOtp(userInformationDTO.getOtp());
        userInformation.setOtpGenerationTime(userInformationDTO.getOtpGenerationTime());
        userInformation.setRole(userInformationDTO.getRole());
        return userInformation;
    }
}