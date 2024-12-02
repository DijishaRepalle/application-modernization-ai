package com.bilvantis.serviceImpl;

import com.bilvantis.exception.DataNotFoundException;
import com.bilvantis.exception.ProjectImplementationSaveFailedException;
import com.bilvantis.model.UserInformation;
import com.bilvantis.model.UserInformationDTO;
import com.bilvantis.repository.UserInformationRepository;
import com.bilvantis.service.UserInformationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import static com.bilvantis.util.ProjectInformationServiceConstants.EXCEPTION_ERROR_MESSAGE;
import static com.bilvantis.util.ProjectInformationServiceConstants.PROJECT_DETAILS_NOT_FOUND;
import static com.bilvantis.util.UserInformationSupport.convertUserEntityTOUserDTO;
import static com.bilvantis.util.UserInformationSupport.convertUsersDTOTOUsersEntity;

@Service
@Slf4j
public class UserInformationServiceImpl implements UserInformationService<UserInformation, UserInformationDTO> {
    public static final String ROLE = "ADMIN";
    private final UserInformationRepository userInformationRepository;

    public UserInformationServiceImpl(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;


    }

    @Override
    public UserInformationDTO createProject(UserInformationDTO userInformationDTO) {
        try {
            if (ObjectUtils.isEmpty(userInformationDTO)) {
                throw new DataNotFoundException(PROJECT_DETAILS_NOT_FOUND);
            }
            UserInformation saveProjectDetails = userInformationRepository.save(convertUsersDTOTOUsersEntity(userInformationDTO));
            return convertUserEntityTOUserDTO(saveProjectDetails);
        } catch (DataAccessException e) {
            log.error(EXCEPTION_ERROR_MESSAGE, this.getClass().getSimpleName(), e.getStackTrace()[0].getMethodName());
            throw new ProjectImplementationSaveFailedException(e.getMessage());
        }
    }


    @Override
    public Boolean getRoleBasedOnUserId(String userId) {
        String role = userInformationRepository.findRoleByUserId(userId);
        return StringUtils.equalsIgnoreCase(role, ROLE);
    }
}
