package com.bilvantis.service;

import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;

public interface UserInformationService<I extends UserInformation, J extends UserInformationDTO> {

    UserInformationDTO createProject(UserInformationDTO userInformationDTO);

    Boolean getRoleBasedOnUserId(String userId);

}
