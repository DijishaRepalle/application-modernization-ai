package com.bilvantis.service;

import com.bilvantis.model.ProjectInformationDTO;
import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;

import java.util.List;
import java.util.Optional;

public interface UserInformationService<I extends UserInformation, J extends UserInformationDTO> {

    Boolean getRoleBasedOnUserId(String userId);

    I createUser(UserInformation user);

    Optional<I> fetchUser(String email);

    I editUser(String email, I updatedUserDetails);

    void deleteUser(String email);

    List<J> getAllUsersInformation();

    ProjectInformationDTO addUsersToProject(String projectCode, List<UserInformationDTO> users);

    ProjectInformationDTO removeTaggedUsersFromProject(String projectCode, List<String> userIds);


}
