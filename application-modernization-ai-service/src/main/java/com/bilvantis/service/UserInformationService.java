package com.bilvantis.service;

import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;

import java.util.Optional;

public interface UserInformationService<I extends UserInformation, J extends UserInformationDTO> {

    UserInformationDTO createProject(UserInformationDTO userInformationDTO);

    Boolean getRoleBasedOnUserId(String userId);

    I createUser(UserInformation user);

    Optional<I> fetchUser(String email);

    I editUser(String email, UserInformation updatedUserDetails);

    void deleteUser(String email);

}
